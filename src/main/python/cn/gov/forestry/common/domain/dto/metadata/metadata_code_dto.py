from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class MetadataCodeDTO:
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    id: Optional[str] = None
    enumId: Optional[str] = None
    codeValue: Optional[str] = None
    codeLabel: Optional[str] = None
    createTime: Optional[datetime] = None
    updateTime: Optional[datetime] = None
    createUserId: Optional[str] = None
    createUserName: Optional[str] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'systemId': self.systemId,
            'systemModule': self.systemModule,
            'id': self.id,
            'enumId': self.enumId,
            'codeValue': self.codeValue,
            'codeLabel': self.codeLabel,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'createUserId': self.createUserId,
            'createUserName': self.createUserName,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataCodeDTO':
        if data is None:
            return None

        def parse_dt(v):
            if v is None:
                return None
            if isinstance(v, str):
                try:
                    return datetime.fromisoformat(v)
                except Exception:
                    return None
            return v

        return cls(
            systemId=data.get('systemId'),
            systemModule=data.get('systemModule'),
            id=data.get('id'),
            enumId=data.get('enumId'),
            codeValue=data.get('codeValue'),
            codeLabel=data.get('codeLabel'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            createUserId=data.get('createUserId'),
            createUserName=data.get('createUserName'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['MetadataCodeDTO']
