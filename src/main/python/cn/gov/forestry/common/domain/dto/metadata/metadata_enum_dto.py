from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class MetadataEnumDTO:
    id: Optional[str] = None
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    enumName: Optional[str] = None
    enumTitleName: Optional[str] = None
    enumAlia: Optional[str] = None
    enumType: Optional[str] = None
    enumGroup: Optional[str] = None
    createTime: Optional[datetime] = None
    updateTime: Optional[datetime] = None
    createUserId: Optional[str] = None
    createUserName: Optional[str] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'id': self.id,
            'systemId': self.systemId,
            'systemModule': self.systemModule,
            'enumName': self.enumName,
            'enumTitleName': self.enumTitleName,
            'enumAlia': self.enumAlia,
            'enumType': self.enumType,
            'enumGroup': self.enumGroup,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'createUserId': self.createUserId,
            'createUserName': self.createUserName,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataEnumDTO':
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
            id=data.get('id'),
            systemId=data.get('systemId'),
            systemModule=data.get('systemModule'),
            enumName=data.get('enumName'),
            enumTitleName=data.get('enumTitleName'),
            enumAlia=data.get('enumAlia'),
            enumType=data.get('enumType'),
            enumGroup=data.get('enumGroup'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            createUserId=data.get('createUserId'),
            createUserName=data.get('createUserName'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['MetadataEnumDTO']
