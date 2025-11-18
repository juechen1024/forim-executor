"""
Python client mirroring Java `ScheduleInnerJobClient` Feign interface.

Methods:
- create_asynchronous_job(ScheduleJobDTO) -> ScheduleJobDTO
- update_asynchronous_job(ScheduleJobDTO) -> int (job id? Long)
- get_schedule_jobs(ScheduleJobDTO) -> List[ScheduleJobDTO]
- create_schedule_job_log(ScheduleJobLogDTO) -> ScheduleJobLogDTO
"""
from typing import Optional, List, Any
import os
import requests

from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO
from cn.gov.forestry.common.domain.dto.schedule.schedule_job_log_dto import ScheduleJobLogDTO


class ScheduleInnerJobClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_SCHEDULE_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_SCHEDULE_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: Any) -> Any:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def create_asynchronous_job(self, dto: ScheduleJobDTO) -> ScheduleJobDTO:
        body = self._post('/inner/schedule/create/asynchronous/job', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return ScheduleJobDTO.from_dict(body)

    def update_asynchronous_job(self, dto: ScheduleJobDTO) -> int:
        body = self._post('/inner/schedule/update/asynchronous/job', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        # expected to return a number (Long in Java). Try to parse.
        if isinstance(body, (int, float)):
            return int(body)
        if isinstance(body, dict) and 'data' in body:
            return int(body['data'])
        raise RuntimeError('Unexpected response format for update_asynchronous_job')

    def get_schedule_jobs(self, dto: ScheduleJobDTO) -> List[ScheduleJobDTO]:
        body = self._post('/inner/schedule/get/asynchronous/job/list', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [ScheduleJobDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [ScheduleJobDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for get_schedule_jobs')

    def create_schedule_job_log(self, dto: ScheduleJobLogDTO) -> ScheduleJobLogDTO:
        body = self._post('/inner/schedule/create/asynchronous/job/log', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return ScheduleJobLogDTO.from_dict(body)


__all__ = ['ScheduleInnerJobClient']
