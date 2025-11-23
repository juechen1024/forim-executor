import re
from typing import Dict, TypeVar

T = TypeVar('T')


class CaseUtil:
    @staticmethod
    def _to_snake(name: str) -> str:
        first = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        second = re.sub('([a-z0-9])([A-Z])', r'\1_\2', first)
        third = second.replace(' ', '_')
        cleaned = re.sub(r'[^0-9a-zA-Z_]+', '_', third)
        return cleaned.lower()

    @staticmethod
    def convertKeysToSnakeCase(values: Dict[str, T]) -> Dict[str, T]:
        return {CaseUtil._to_snake(key): value for key, value in (values or {}).items()}


__all__ = ["CaseUtil"]
