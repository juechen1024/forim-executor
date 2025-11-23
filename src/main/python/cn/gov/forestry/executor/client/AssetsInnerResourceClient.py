"""Python adaptation of the Java `AssetsInnerResourceClient` interface.

Methods mirror the Java naming style (camelCase):
- putResourceFile(FileContent) -> FileContent
- getResourceFile(FileContent) -> FileContent
"""
from typing import Optional
import os
import requests

from cn.gov.forestry.common.file.file_content import FileContent


class AssetsInnerResourceClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_ASSETS_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_ASSETS_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> dict:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def putResourceFile(self, fileContent: FileContent) -> FileContent:  # Java-style name
        data = fileContent.to_dict()
        body = self._post('/inner/resource/put/file', data)
        return FileContent.from_dict(body)

    def getResourceFile(self, fileContent: FileContent) -> FileContent:  # Java-style name
        data = fileContent.to_dict()
        body = self._post('/inner/resource/get/file', data)
        return FileContent.from_dict(body)


__all__ = ['AssetsInnerResourceClient']
