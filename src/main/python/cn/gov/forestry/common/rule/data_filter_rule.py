from dataclasses import dataclass
from typing import Any, Dict, Optional


@dataclass
class DataFilterRule:
    # Minimal placeholder to satisfy imports by DTOs.
    ruleType: Optional[str] = None
    ruleValue: Optional[Any] = None

    def to_dict(self) -> Dict[str, Any]:
        return {
            'ruleType': self.ruleType,
            'ruleValue': self.ruleValue,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'DataFilterRule':
        if data is None:
            return None
        return cls(ruleType=data.get('ruleType'), ruleValue=data.get('ruleValue'))
