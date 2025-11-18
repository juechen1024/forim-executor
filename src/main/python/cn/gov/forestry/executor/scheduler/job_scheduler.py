"""
Python version of `JobScheduler`.

This module provides `JobScheduler.check_job_status()` which mirrors the
behavior of the Java `JobScheduler.checkJobStatus()`:
- Query running jobs; if none, query init jobs and execute the first one.

Scheduling: the Java class used Spring's `@Scheduled(cron="0 */1 * * * ?")`.
In Python you can run `check_job_status` periodically using `APScheduler`, a
system cron, or a simple timer. Example usage is included in the module docstring.
"""
import logging
from typing import List, Optional

from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO
from cn.gov.forestry.executor.client.schedule_inner_job_client import ScheduleInnerJobClient
from cn.gov.forestry.executor.job.schedule_job_executor import ScheduleJobExecutor

logger = logging.getLogger(__name__)

# Simple status codes matching Java enum SystemScheduleJobStatusEnum
RUNNING_CODE = "10"
INIT_CODE = "0"


class JobScheduler:
    def __init__(self, schedule_client: ScheduleInnerJobClient, executor: ScheduleJobExecutor):
        self.schedule_client = schedule_client
        self.executor = executor

    def check_job_status(self) -> None:
        """Check running jobs and start an init job if none are running.

        This mirrors the Java behavior:
        - Query jobs with status RUNNING. If any exist, log count and exit.
        - Otherwise, query jobs with status INIT and execute the first one.
        """
        logger.info("JobScheduler-start-cron-checking-job-status.....")

        running_param = ScheduleJobDTO()
        running_param.jobStatus = RUNNING_CODE
        try:
            running_jobs: Optional[List[ScheduleJobDTO]] = self.schedule_client.get_schedule_jobs(running_param)
        except Exception as e:
            logger.exception("Failed to query running jobs")
            return

        if not running_jobs:
            logger.info("JobScheduler-no-running-jobs-start-checking-init-jobs")
            init_param = ScheduleJobDTO()
            init_param.jobStatus = INIT_CODE
            try:
                init_jobs = self.schedule_client.get_schedule_jobs(init_param)
            except Exception:
                logger.exception("Failed to query init jobs")
                return

            if init_jobs:
                logger.info(f"JobScheduler-starting-init-job-[{init_jobs[0].id}]...")
                try:
                    self.executor.execute(init_jobs[0])
                except Exception:
                    logger.exception("Failed to execute init job")
            else:
                logger.info("JobScheduler-no-init-jobs")
        else:
            logger.info(f"JobScheduler-running-jobs-count-[{len(running_jobs)}]")

        logger.info("JobScheduler-end-cron")


# Optional helper: run check_job_status every N seconds using threading.Timer
def schedule_periodic_check(scheduler: JobScheduler, interval_seconds: int = 60):
    """Run `scheduler.check_job_status` every `interval_seconds` seconds in background.

    Note: this is a simple helper for quick testing. For production use
    prefer `APScheduler`, `celery beat`, or system cron.
    """
    import threading

    def _run():
        try:
            scheduler.check_job_status()
        finally:
            threading.Timer(interval_seconds, _run).start()

    threading.Timer(interval_seconds, _run).start()


__all__ = ['JobScheduler', 'schedule_periodic_check']
