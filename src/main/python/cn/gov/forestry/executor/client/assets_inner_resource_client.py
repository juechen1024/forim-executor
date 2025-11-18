"""
Python client mirroring Java `AssetsInnerResourceClient` Feign interface.

Provides `put_resource_file` and `get_resource_file` methods that send JSON
representations of `FileContent` to the configured service.
"""
from typing import Optional
import os
import requests

from cn.gov.forestry.common.file.file_content import FileContent


class AssetsInnerResourceClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        """Create client.

        Args:
            base_url: service base URL, e.g. "http://host:port". If not provided,
                      will read from env `FORIM_INNER_ASSETS_URL`.
            timeout: request timeout in seconds.
        """
        self.base_url = base_url or os.getenv('FORIM_INNER_ASSETS_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_ASSETS_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> dict:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def put_resource_file(self, file_content: FileContent) -> FileContent:
        """POST `/inner/resource/put/file` with `FileContent` JSON and return result."""
        data = file_content.to_dict()
        body = self._post('/inner/resource/put/file', data)
        return FileContent.from_dict(body)

    def get_resource_file(self, file_content: FileContent) -> FileContent:
        """POST `/inner/resource/get/file` with `FileContent` JSON and return result."""
        data = file_content.to_dict()
        body = self._post('/inner/resource/get/file', data)
        return FileContent.from_dict(body)


__all__ = ['AssetsInnerResourceClient']
