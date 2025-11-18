from dataclasses import dataclass
from typing import Any, Optional, Dict


@dataclass
class FieldValue:
    dataTypeCode: Optional[str] = None
    value: Optional[Any] = None

    def to_dict(self) -> Dict:
        return {
            'dataTypeCode': self.dataTypeCode,
            'value': self.value,
        }

    @classmethod
    def from_dict(cls, data: Dict) -> 'FieldValue':
        if data is None:
            return None
        return cls(
            dataTypeCode=data.get('dataTypeCode'),
            value=data.get('value'),
        )


__all__ = ['FieldValue']
