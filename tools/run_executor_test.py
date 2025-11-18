"""
Safe smoke test for executor application.
This script constructs the executor and scheduler using the real builders,
then replaces the schedule client with a dummy implementation that returns
empty lists (no network access). It calls `check_job_status()` once.
"""
import os
import sys

# Ensure local source is on path
root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
src_py = os.path.join(root, 'src', 'main', 'python')
if src_py not in sys.path:
    sys.path.insert(0, src_py)

from cn.gov.forestry.executor.executor_application import create_executor_and_scheduler
from cn.gov.forestry.executor.scheduler.job_scheduler import JobScheduler


class DummyScheduleClient:
    def get_schedule_jobs(self, param):
        # Return empty list to simulate no running/init jobs
        return []

    def update_asynchronous_job(self, dto):
        return None

    def create_schedule_job_log(self, log):
        return None


if __name__ == '__main__':
    # Build a clients dict with dummy implementations to avoid any network calls
    clients = {
        'general': type('G', (), {'get_system_info': lambda self, q: None})(),
        'assets': type('A', (), {'get_resource_file': lambda self, f: None})(),
        'db': type('D', (), {'insert_batch': lambda self, p: []})(),
        'metadata_q': type('MQ', (), {'get_metadata_table_info': lambda self, q: None, 'get_metadata_field_list': lambda self, q: []})(),
        'metadata_o': type('MO', (), {'update_metadata_table_additional_properties': lambda self, d: None, 'create_field_batch': lambda self, d: None})(),
        'schedule': DummyScheduleClient(),
    }

    executor, scheduler = create_executor_and_scheduler(clients)
    print('Running scheduler.check_job_status() once...')
    try:
        scheduler.check_job_status()
        print('check_job_status completed')
    except Exception as e:
        print('check_job_status raised:', type(e).__name__, e)
