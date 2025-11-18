"""
Run a safe read-only smoke test against configured FORIM_INNER_* services.
This script performs only query calls (no update, create, or delete).

Usage (PowerShell):
$env:FORIM_INNER_GENERAL_URL='http://192.168.7.2:31380'; \
$env:FORIM_INNER_SCHEDULE_URL='http://192.168.7.2:31380'; \
$env:PYTHONPATH='src/main/python'; python tools/run_real_read_test.py

The script queries schedule jobs (RUNNING and INIT) and prints results.
"""
import os
import sys
import json

root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
src_py = os.path.join(root, 'src', 'main', 'python')
if src_py not in sys.path:
    sys.path.insert(0, src_py)

from cn.gov.forestry.executor.client.general_inner_query_client import GeneralInnerQueryClient
from cn.gov.forestry.executor.client.schedule_inner_job_client import ScheduleInnerJobClient
from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO


def safe_print(obj):
    try:
        print(json.dumps(obj, ensure_ascii=False, indent=2))
    except Exception:
        print(obj)


def main():
    # Build clients from env vars (executor_application does similar)
    general_url = os.environ.get('FORIM_INNER_GENERAL_URL')
    schedule_url = os.environ.get('FORIM_INNER_SCHEDULE_URL')

    print('Using general_url=', general_url)
    print('Using schedule_url=', schedule_url)

    if not schedule_url and not general_url:
        print('No FORIM_INNER_* urls provided in environment. Aborting.')
        return

    # Construct clients (they will raise if no base_url)
    general_client = GeneralInnerQueryClient(base_url=general_url) if general_url else None
    schedule_client = ScheduleInnerJobClient(base_url=schedule_url) if schedule_url else None

    # Query running jobs
    if schedule_client:
        try:
            p = ScheduleJobDTO()
            p.jobStatus = '10'  # RUNNING
            running = schedule_client.get_schedule_jobs(p)
            print('Running jobs count:', len(running) if running else 0)
            if running:
                print('First running job id:', getattr(running[0], 'id', None))
        except Exception as e:
            print('Error querying running jobs:', type(e).__name__, e)

        # Query init jobs
        try:
            p2 = ScheduleJobDTO()
            p2.jobStatus = '0'  # INIT
            init_jobs = schedule_client.get_schedule_jobs(p2)
            print('Init jobs count:', len(init_jobs) if init_jobs else 0)
            if init_jobs:
                print('First init job id:', getattr(init_jobs[0], 'id', None))
        except Exception as e:
            print('Error querying init jobs:', type(e).__name__, e)

    # Optionally call a read-only general query if configured
    if general_client:
        try:
            q = None
            # Not calling get_system_info without a known id to avoid unexpected behavior
            print('Skipping general_client.get_system_info to avoid unknown read')
        except Exception as e:
            print('Error during general client read:', type(e).__name__, e)


if __name__ == '__main__':
    main()
