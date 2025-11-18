"""
Python client mirroring Java `ExchangeInnerTransferClient` Feign interface.

Provides methods:
- transfer_excel_to_list
- transfer_shapefile_zip_to_list
- transfer_geo_tiff_to_list
- transfer_list_to_excel_template
- transfer_list_to_excel_content

All POST JSON and return either a list of dicts or `FileContent` objects.
"""
from typing import List, Dict, Any, Optional
import os
import requests

from cn.gov.forestry.common.file.file_content import FileContent


class ExchangeInnerTransferClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_EXCHANGE_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_EXCHANGE_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> Any:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def _extract_list(self, body: Any) -> List[Dict[str, Any]]:
        if isinstance(body, list):
            return body
        if isinstance(body, dict):
            for key in ('data', 'result', 'items'):
                if key in body and isinstance(body[key], list):
                    return body[key]
        raise RuntimeError('Unexpected response format: expected list')

    def transfer_excel_to_list(self, file_content: FileContent) -> List[Dict[str, Any]]:
        """POST `/inner/transfer/content/excel/to/list` with `FileContent` and return list of records."""
        payload = file_content.to_dict()
        body = self._post('/inner/transfer/content/excel/to/list', payload)
        return self._extract_list(body)

    def transfer_shapefile_zip_to_list(self, file_content: FileContent) -> List[Dict[str, Any]]:
        """POST `/inner/transfer/content/shapefile/zip/to/list` with `FileContent` and return list."""
        payload = file_content.to_dict()
        body = self._post('/inner/transfer/content/shapefile/zip/to/list', payload)
        return self._extract_list(body)

    def transfer_geo_tiff_to_list(self, file_content: FileContent) -> List[Dict[str, Any]]:
        """POST `/inner/transfer/content/geo/tiff/to/list` with `FileContent` and return list."""
        payload = file_content.to_dict()
        body = self._post('/inner/transfer/content/geo/tiff/to/list', payload)
        return self._extract_list(body)

    def transfer_list_to_excel_template(self, data: Dict[str, Any]) -> FileContent:
        """POST `/inner/transfer/content/list/to/excel/template` with a data map and return `FileContent`."""
        body = self._post('/inner/transfer/content/list/to/excel/template', data)
        return FileContent.from_dict(body)

    def transfer_list_to_excel_content(self, data: Dict[str, Any]) -> FileContent:
        """POST `/inner/transfer/content/list/to/excel/content` with a data map and return `FileContent`."""
        body = self._post('/inner/transfer/content/list/to/excel/content', data)
        return FileContent.from_dict(body)


__all__ = ['ExchangeInnerTransferClient']
