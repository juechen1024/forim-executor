"""
Python client mirroring Java `DatabaseInnerCRUDClient` Feign interface.

Provides `insert_batch` which posts `InsertBatchParams` JSON to
`/inner/crud/insert/batch` and returns a list of string IDs (or None for failures).
"""
from typing import List, Optional
import os
import requests

from cn.gov.forestry.common.database.crud.insert_batch_params import InsertBatchParams


class DatabaseInnerCRUDClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 30):
        self.base_url = base_url or os.getenv('FORIM_INNER_DATABASE_URL')
        if not self.base_url:
            raise ValueError('base_url must be provided or FORIM_INNER_DATABASE_URL set')
        self.timeout = timeout

    def _post(self, path: str, payload: dict) -> dict:
        url = self.base_url.rstrip('/') + path
        resp = requests.post(url, json=payload, timeout=self.timeout)
        resp.raise_for_status()
        return resp.json()

    def insert_batch(self, params: InsertBatchParams) -> List[Optional[str]]:
        """POST `/inner/crud/insert/batch` and return list of inserted IDs.

        Args:
            params: `InsertBatchParams` instance.

        Returns:
            List of string IDs; elements may be `None` for failed inserts.
        """
        payload = params.to_dict() if hasattr(params, 'to_dict') else params
        body = self._post('/inner/crud/insert/batch', payload)
        # Expecting JSON array of strings (or nulls). If server wraps response,
        # user may need to adapt this method.
        if isinstance(body, list):
            return body
        # If server returns an object like {"data": [...]}, try to extract
        if isinstance(body, dict):
            for key in ('data', 'result', 'items'):
                if key in body and isinstance(body[key], list):
                    return body[key]
        raise RuntimeError('Unexpected response format from insert_batch')


__all__ = ['DatabaseInnerCRUDClient']
