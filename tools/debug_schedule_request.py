"""
Debug schedule service requests: send the same requests as ScheduleInnerJobClient
but print full request/response details (URL, body, status, response body).
Only POST read requests will be sent; no update/create calls.

Usage (PowerShell):
$env:PYTHONPATH='src/main/python'; $env:FORIM_INNER_SCHEDULE_URL='http://192.168.7.2:31380'; python tools/debug_schedule_request.py
"""
import os
import sys
import json
import requests

root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
src_py = os.path.join(root, 'src', 'main', 'python')
if src_py not in sys.path:
    sys.path.insert(0, src_py)

from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO


def pprint_resp(resp: requests.Response):
    print('--- RESPONSE ---')
    print('Status:', resp.status_code)
    print('Headers:')
    for k, v in resp.headers.items():
        print('  ', k, ':', v)
    print('Body:')
    try:
        print(json.dumps(resp.json(), ensure_ascii=False, indent=2))
    except Exception:
        print(resp.text[:1000])
    print('--- END RESPONSE ---')


def do_post(base_url: str, path: str, payload: dict):
    url = base_url.rstrip('/') + path
    print('\n>>> POST', url)
    print('Payload:', json.dumps(payload, ensure_ascii=False))
    try:
        resp = requests.post(url, json=payload, timeout=10)
        pprint_resp(resp)
    except Exception as e:
        print('Request error:', type(e).__name__, e)


def main():
    schedule_url = os.environ.get('FORIM_INNER_SCHEDULE_URL')
    if not schedule_url:
        print('Set FORIM_INNER_SCHEDULE_URL env var first')
        return

    # Try get_schedule_jobs with RUNNING
    p = ScheduleJobDTO()
    p.jobStatus = '10'
    do_post(schedule_url, '/inner/schedule/get/asynchronous/job/list', p.to_dict())

    # Try get_schedule_jobs with INIT
    p2 = ScheduleJobDTO()
    p2.jobStatus = '0'
    do_post(schedule_url, '/inner/schedule/get/asynchronous/job/list', p2.to_dict())

if __name__ == '__main__':
    main()
