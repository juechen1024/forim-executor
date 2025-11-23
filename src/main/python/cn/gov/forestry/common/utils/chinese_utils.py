import re
import unicodedata
from typing import Any, Dict


class ChineseUtils:
    @staticmethod
    def _simple_slug(text: str) -> str:
        normalized = unicodedata.normalize('NFKD', text)
        ascii_only = ''.join(ch for ch in normalized if ord(ch) < 128)
        cleaned = re.sub(r'[^0-9a-zA-Z]+', '_', ascii_only).strip('_').lower()
        return cleaned or 'field'

    @staticmethod
    def convertKeysToPinyinWithUniqueSuffix(properties: Dict[str, Any]) -> Dict[str, Any]:
        if not properties:
            return {}

        try:
            from pypinyin import lazy_pinyin

            def to_pinyin(token: str) -> str:
                return ''.join(lazy_pinyin(token))
        except Exception:
            to_pinyin = None

        result: Dict[str, Any] = {}
        seen = {}
        for key, value in properties.items():
            if to_pinyin:
                converted = to_pinyin(key)
                converted = re.sub(r'[^0-9a-zA-Z]+', '_', converted).lower()
                if not converted:
                    converted = ChineseUtils._simple_slug(key)
            else:
                converted = ChineseUtils._simple_slug(key)

            base = converted
            suffix = seen.get(base, 0)
            if suffix:
                converted = f"{base}_{suffix}"
            seen[base] = suffix + 1
            result[converted] = value
        return result


__all__ = ["ChineseUtils"]
