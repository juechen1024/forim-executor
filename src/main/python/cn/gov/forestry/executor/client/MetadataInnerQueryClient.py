"""Python adaptation of Java `MetadataInnerQueryClient`.

Method names converted to Java camelCase:
- getMetadataTableInfo(MetadataTableDTO) -> MetadataTableDTO
- getMetadataTablePermission(MetadataTablePermissionDTO) -> MetadataTablePermissionDTO
- getMetadataTableList(MetadataTableDTO) -> List[MetadataTableDTO]
- getMetadataFieldInfo(MetadataFieldDTO) -> MetadataFieldDTO
- getMetadataFieldListByTable(MetadataFieldDTO) -> List[MetadataFieldDTO]
- getMetadataEnumInfo(MetadataEnumDTO) -> MetadataEnumDTO
- getMetadataCodeListByEnum(MetadataCodeDTO) -> List[MetadataCodeDTO]
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

    def getMetadataTableInfo(self, dto: MetadataTableDTO) -> MetadataTableDTO:
        body = self._post('/inner/query/metadata/sys/table/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataTableDTO.from_dict(body)

    def getMetadataTablePermission(self, dto: MetadataTablePermissionDTO) -> MetadataTablePermissionDTO:
        body = self._post('/inner/query/metadata/sys/table/permission', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataTablePermissionDTO.from_dict(body)

    def getMetadataTableList(self, dto: MetadataTableDTO) -> List[MetadataTableDTO]:
        body = self._post('/inner/query/metadata/sys/table/list', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataTableDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataTableDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for getMetadataTableList')

    def getMetadataFieldInfo(self, dto: MetadataFieldDTO) -> MetadataFieldDTO:
        body = self._post('/inner/query/metadata/sys/field/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataFieldDTO.from_dict(body)

    def getMetadataFieldListByTable(self, dto: MetadataFieldDTO) -> List[MetadataFieldDTO]:
        body = self._post('/inner/query/metadata/sys/field/list/by/table', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataFieldDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataFieldDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for getMetadataFieldListByTable')

    def getMetadataEnumInfo(self, dto: MetadataEnumDTO) -> MetadataEnumDTO:
        body = self._post('/inner/query/metadata/sys/enum/info', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataEnumDTO.from_dict(body)

    def getMetadataCodeListByEnum(self, dto: MetadataCodeDTO) -> List[MetadataCodeDTO]:
        body = self._post('/inner/query/metadata/sys/code/list/by/enum', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        if isinstance(body, list):
            return [MetadataCodeDTO.from_dict(x) for x in body]
        if isinstance(body, dict) and 'data' in body and isinstance(body['data'], list):
            return [MetadataCodeDTO.from_dict(x) for x in body['data']]
        raise RuntimeError('Unexpected response format for getMetadataCodeListByEnum')


__all__ = ['MetadataInnerQueryClient']
