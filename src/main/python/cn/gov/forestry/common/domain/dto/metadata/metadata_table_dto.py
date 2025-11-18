from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class MetadataTableDTO:
    id: Optional[str] = None
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    tableName: Optional[str] = None
    tableTitleName: Optional[str] = None
    tableAlia: Optional[str] = None
    tableType: Optional[str] = None
    tableGroup: Optional[str] = None
    tableTheme: Optional[str] = None
    tableActionType: Optional[str] = None
    tableGeometryType: Optional[str] = None
    isPublishedAsEnum: Optional[bool] = None
    asEnumLabelFieldId: Optional[str] = None
    asEnumValueFieldId: Optional[str] = None
    auditLogLevel: Optional[int] = None
    tableEntityName: Optional[str] = None
    tableFieldCounts: Optional[int] = None
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
            'tableName': self.tableName,
            'tableTitleName': self.tableTitleName,
            'tableAlia': self.tableAlia,
            'tableType': self.tableType,
            'tableGroup': self.tableGroup,
            'tableTheme': self.tableTheme,
            'tableActionType': self.tableActionType,
            'tableGeometryType': self.tableGeometryType,
            'isPublishedAsEnum': self.isPublishedAsEnum,
            'asEnumLabelFieldId': self.asEnumLabelFieldId,
            'asEnumValueFieldId': self.asEnumValueFieldId,
            'auditLogLevel': self.auditLogLevel,
            'tableEntityName': self.tableEntityName,
            'tableFieldCounts': self.tableFieldCounts,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'createUserId': self.createUserId,
            'createUserName': self.createUserName,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataTableDTO':
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
            tableName=data.get('tableName'),
            tableTitleName=data.get('tableTitleName'),
            tableAlia=data.get('tableAlia'),
            tableType=data.get('tableType'),
            tableGroup=data.get('tableGroup'),
            tableTheme=data.get('tableTheme'),
            tableActionType=data.get('tableActionType'),
            tableGeometryType=data.get('tableGeometryType'),
            isPublishedAsEnum=data.get('isPublishedAsEnum'),
            asEnumLabelFieldId=data.get('asEnumLabelFieldId'),
            asEnumValueFieldId=data.get('asEnumValueFieldId'),
            auditLogLevel=data.get('auditLogLevel'),
            tableEntityName=data.get('tableEntityName'),
            tableFieldCounts=data.get('tableFieldCounts'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            createUserId=data.get('createUserId'),
            createUserName=data.get('createUserName'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['MetadataTableDTO']
