"""
Python client mirroring Java `GeneralInnerQueryClient` Feign interface.

Methods:
- get_system_info(GeneralSystemDTO) -> GeneralSystemDTO
- query_user_info_by_token(GeneralUserDTO) -> GeneralUserDTO
"""
from typing import Optional
import os
import requests

from cn.gov.forestry.common.domain.dto.general.general_system_dto import GeneralSystemDTO
from cn.gov.forestry.common.domain.dto.general.general_user_dto import GeneralUserDTO


class GeneralInnerQueryClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_GENERAL_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_GENERAL_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> dict:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def get_system_info(self, dto: GeneralSystemDTO) -> GeneralSystemDTO:
        body = self._post('/inner/query/general/system/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        # body may be dict representing GeneralSystemDTO
        return GeneralSystemDTO.from_dict(body)

    def query_user_info_by_token(self, dto: GeneralUserDTO) -> GeneralUserDTO:
        body = self._post('/inner/query/general/user/info/by/token', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return GeneralUserDTO.from_dict(body)


__all__ = ['GeneralInnerQueryClient']
