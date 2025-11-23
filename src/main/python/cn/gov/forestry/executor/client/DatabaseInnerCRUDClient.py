"""Python adaptation of Java `DatabaseInnerCRUDClient`.

Method naming aligned with Java (camelCase):
- insertBatch(InsertBatchParams) -> List[str]
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

    def insertBatch(self, params: InsertBatchParams) -> List[Optional[str]]:  # Java-style name
        payload = params.to_dict() if hasattr(params, 'to_dict') else params
        body = self._post('/inner/crud/insert/batch', payload)
        if isinstance(body, list):
            return body
        if isinstance(body, dict):
            for key in ('data', 'result', 'items'):
                if key in body and isinstance(body[key], list):
                    return body[key]
        raise RuntimeError('Unexpected response format from insertBatch')


__all__ = ['DatabaseInnerCRUDClient']
