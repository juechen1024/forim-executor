"""Python adaptation of Java `ExchangeInnerTransferClient`.

Method names converted to Java camelCase to mirror Feign interface:
- transferExcelToList(FileContent) -> List[Map]
- transferShapefileZipToList(FileContent) -> List[Map]
- transferGeoTiffToList(FileContent) -> List[Map]
- transferListToExcelTemplate(Map) -> FileContent
- transferListToExcelContent(Map) -> FileContent
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

    def transferExcelToList(self, fileContent: FileContent) -> List[Dict[str, Any]]:
        payload = fileContent.to_dict()
        body = self._post('/inner/transfer/content/excel/to/list', payload)
        return self._extract_list(body)

    def transferShapefileZipToList(self, fileContent: FileContent) -> List[Dict[str, Any]]:
        payload = fileContent.to_dict()
        body = self._post('/inner/transfer/content/shapefile/zip/to/list', payload)
        return self._extract_list(body)

    def transferGeoTiffToList(self, fileContent: FileContent) -> List[Dict[str, Any]]:
        payload = fileContent.to_dict()
        body = self._post('/inner/transfer/content/geo/tiff/to/list', payload)
        return self._extract_list(body)

    def transferListToExcelTemplate(self, data: Dict[str, Any]) -> FileContent:
        body = self._post('/inner/transfer/content/list/to/excel/template', data)
        return FileContent.from_dict(body)

    def transferListToExcelContent(self, data: Dict[str, Any]) -> FileContent:
        body = self._post('/inner/transfer/content/list/to/excel/content', data)
        return FileContent.from_dict(body)


__all__ = ['ExchangeInnerTransferClient']
