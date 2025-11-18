"""
Python client mirroring Java `MetadataInnerOptClient` Feign interface.

Methods:
- update_metadata_table_additional_properties(MetadataTableDTO) -> MetadataTableDTO
- create_field(MetadataFieldDTO) -> MetadataFieldDTO
- create_field_batch(MetadataFieldBatchDTO) -> MetadataFieldBatchDTO
"""
from typing import Optional
import os
import requests

from cn.gov.forestry.common.domain.dto.metadata.metadata_table_dto import MetadataTableDTO
from cn.gov.forestry.common.domain.dto.metadata.metadata_field_dto import MetadataFieldDTO
from cn.gov.forestry.common.domain.dto.metadata.batch.metadata_field_batch_dto import MetadataFieldBatchDTO


class MetadataInnerOptClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_METADATA_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_METADATA_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> dict:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def update_metadata_table_additional_properties(self, dto: MetadataTableDTO) -> MetadataTableDTO:
        body = self._post('/inner/operator/metadata/sys/update/table/additional/properties', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataTableDTO.from_dict(body)

    def create_field(self, dto: MetadataFieldDTO) -> MetadataFieldDTO:
        body = self._post('/inner/operator/metadata/sys/create/field', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataFieldDTO.from_dict(body)

    def create_field_batch(self, dto: MetadataFieldBatchDTO) -> MetadataFieldBatchDTO:
        body = self._post('/inner/operator/metadata/sys/create/field/batch', dto.to_dict() if hasattr(dto, 'to_dict') else dto)
        return MetadataFieldBatchDTO.from_dict(body)


__all__ = ['MetadataInnerOptClient']
