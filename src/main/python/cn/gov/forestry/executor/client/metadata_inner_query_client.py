"""
Python client mirroring Java `MetadataInnerQueryClient` Feign interface.

Methods provided mirror the Java interface:
- get_metadata_table_info -> MetadataTableDTO
- get_metadata_table_permission -> MetadataTablePermissionDTO
- get_metadata_table_list -> List[MetadataTableDTO]
- get_metadata_field_info -> MetadataFieldDTO
- get_metadata_field_list_by_table -> List[MetadataFieldDTO]
- get_metadata_enum_info -> MetadataEnumDTO
- get_metadata_code_list_by_enum -> List[MetadataCodeDTO]

"""
from typing import Optional, List, Dict, Any
import os
import requests

from cn.gov.forestry.common.domain.dto.metadata.metadata_table_dto import MetadataTableDTO
from cn.gov.forestry.common.domain.dto.metadata.metadata_table_permission_dto import MetadataTablePermissionDTO
from cn.gov.forestry.common.domain.dto.metadata.metadata_field_dto import MetadataFieldDTO
from cn.gov.forestry.common.domain.dto.metadata.metadata_enum_dto import MetadataEnumDTO
from cn.gov.forestry.common.domain.dto.metadata.metadata_code_dto import MetadataCodeDTO


class MetadataInnerQueryClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_METADATA_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_METADATA_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> Any:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def get_metadata_table_info(self, dto: MetadataTableDTO) -> MetadataTableDTO:
        body = self._post('/inner/query/metadata/sys/table/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataTableDTO.from_dict(body)

    def get_metadata_table_permission(self, dto: MetadataTablePermissionDTO) -> MetadataTablePermissionDTO:
        body = self._post('/inner/query/metadata/sys/table/permission', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataTablePermissionDTO.from_dict(body)

    def get_metadata_table_list(self, dto: MetadataTableDTO) -> List[MetadataTableDTO]:
        body = self._post('/inner/query/metadata/sys/table/list', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataTableDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataTableDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for get_metadata_table_list')

    def get_metadata_field_info(self, dto: MetadataFieldDTO) -> MetadataFieldDTO:
        body = self._post('/inner/query/metadata/sys/field/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataFieldDTO.from_dict(body)

    def get_metadata_field_list_by_table(self, dto: MetadataFieldDTO) -> List[MetadataFieldDTO]:
        body = self._post('/inner/query/metadata/sys/field/list/by/table', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataFieldDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataFieldDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for get_metadata_field_list_by_table')

    def get_metadata_enum_info(self, dto: MetadataEnumDTO) -> MetadataEnumDTO:
        body = self._post('/inner/query/metadata/sys/enum/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataEnumDTO.from_dict(body)

    def get_metadata_code_list_by_enum(self, dto: MetadataCodeDTO) -> List[MetadataCodeDTO]:
        body = self._post('/inner/query/metadata/sys/code/list/by/enum', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataCodeDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataCodeDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for get_metadata_code_list_by_enum')


__all__ = ['MetadataInnerQueryClient']
