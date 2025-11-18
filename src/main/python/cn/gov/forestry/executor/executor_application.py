"""
Python application entry for the executor service.

This module wires the inner service clients, the job executor, and the
JobScheduler. It provides `run()` to start the scheduler loop. For a
production-ready deployment you may run this module under a process manager
or container; for testing you can call `run()` directly.

Usage examples:
- Run with simple timer (no extra deps):
    python -c "from cn.gov.forestry.executor.executor_application import run; run()"

- Run with APScheduler (recommended for production scheduling):
    pip install apscheduler
    python -c "from cn.gov.forestry.executor.executor_application import run_with_apscheduler; run_with_apscheduler()"
"""
import logging
import os
from typing import Optional
from dotenv import load_dotenv

from cn.gov.forestry.executor.client.schedule_inner_job_client import ScheduleInnerJobClient
from cn.gov.forestry.executor.job.schedule_job_executor import ScheduleJobExecutor
from cn.gov.forestry.executor.scheduler.job_scheduler import JobScheduler, schedule_periodic_check

# Other clients can be constructed similarly and injected into ScheduleJobExecutor
from cn.gov.forestry.executor.client.general_inner_query_client import GeneralInnerQueryClient
from cn.gov.forestry.executor.client.assets_inner_resource_client import AssetsInnerResourceClient
from cn.gov.forestry.executor.client.database_inner_crud_client import DatabaseInnerCRUDClient
from cn.gov.forestry.executor.client.metadata_inner_query_client import MetadataInnerQueryClient
from cn.gov.forestry.executor.client.metadata_inner_opt_client import MetadataInnerOptClient

logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)


def build_clients() -> dict:
    """Construct inner service clients using environment variables when available.

    Each client reads its base URL from an env var (see client constructors).
    """
    # Load .env from repository root (if present)
    load_dotenv()

    general_url = os.getenv('FORIM_INNER_GENERAL_URL')
    assets_url = os.getenv('FORIM_INNER_ASSETS_URL')
    database_url = os.getenv('FORIM_INNER_DATABASE_URL')
    metadata_url = os.getenv('FORIM_INNER_METADATA_URL')
    exchange_url = os.getenv('FORIM_INNER_EXCHANGE_URL')
    schedule_url = os.getenv('FORIM_INNER_SCHEDULE_URL')

    general = GeneralInnerQueryClient(base_url=general_url)
    assets = AssetsInnerResourceClient(base_url=assets_url)
    db = DatabaseInnerCRUDClient(base_url=database_url)
    metadata_q = MetadataInnerQueryClient(base_url=metadata_url)
    metadata_o = MetadataInnerOptClient(base_url=metadata_url)
    schedule = ScheduleInnerJobClient(base_url=schedule_url)

    return dict(general=general, assets=assets, db=db, metadata_q=metadata_q, metadata_o=metadata_o, schedule=schedule)


def create_executor_and_scheduler(clients: Optional[dict] = None):
    if clients is None:
        clients = build_clients()

    executor = ScheduleJobExecutor(
        general_client=clients['general'],
        assets_client=clients['assets'],
        db_client=clients['db'],
        metadata_query_client=clients['metadata_q'],
        metadata_opt_client=clients['metadata_o'],
        schedule_client=clients['schedule'],
    )

    scheduler = JobScheduler(schedule_client=clients['schedule'], executor=executor)
    return executor, scheduler


def run(interval_seconds: int = 60):
    """Start the simple periodic scheduler using `threading.Timer` helper.

    This requires no extra dependency and is useful for local testing.
    """
    _, scheduler = create_executor_and_scheduler()
    logger.info('Starting simple scheduler with interval %s seconds', interval_seconds)
    schedule_periodic_check(scheduler, interval_seconds)


def run_with_apscheduler(interval_seconds: int = 60):
    """Start the scheduler using APScheduler.

    Install with: pip install apscheduler
    """
    try:
        from apscheduler.schedulers.background import BackgroundScheduler
    except Exception:
        raise RuntimeError('APScheduler not installed. Run `pip install apscheduler`')

    _, scheduler = create_executor_and_scheduler()
    aps = BackgroundScheduler()
    aps.add_job(lambda: scheduler.check_job_status(), 'interval', seconds=interval_seconds)
    aps.start()
    logger.info('APScheduler started; checking every %s seconds', interval_seconds)
    try:
        # keep main thread alive
        import time
        while True:
            time.sleep(3600)
    except (KeyboardInterrupt, SystemExit):
        aps.shutdown()


if __name__ == '__main__':
    # Allow overriding interval via env var
    try:
        interval = int(os.getenv('EXECUTOR_CHECK_INTERVAL', '60'))
    except Exception:
        interval = 60
    run(interval)
