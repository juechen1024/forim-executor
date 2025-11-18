from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class MetadataFieldDTO:
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    id: Optional[str] = None
    tableId: Optional[str] = None
    fieldName: Optional[str] = None
    fieldTitleName: Optional[str] = None
    fieldAlia: Optional[str] = None
    fieldType: Optional[str] = None
    fieldDataType: Optional[str] = None
    fieldGeometryType: Optional[str] = None
    fieldDataLength: Optional[str] = None
    fieldDefaultValue: Optional[str] = None
    fieldOrder: Optional[int] = None
    fieldSortOrder: Optional[int] = None
    fieldEnumId: Optional[str] = None
    fieldFromUserProperties: Optional[str] = None
    isPrimaryKey: Optional[bool] = None
    isNullable: Optional[bool] = None
    isOrderField: Optional[bool] = None
    # fieldPaint and fieldSource are represented as generic dicts if present
    fieldPaint: Optional[Dict[str, Any]] = None
    fieldSource: Optional[Dict[str, Any]] = None
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
            'tableId': self.tableId,
            'fieldName': self.fieldName,
            'fieldTitleName': self.fieldTitleName,
            'fieldAlia': self.fieldAlia,
            'fieldType': self.fieldType,
            'fieldDataType': self.fieldDataType,
            'fieldGeometryType': self.fieldGeometryType,
            'fieldDataLength': self.fieldDataLength,
            'fieldDefaultValue': self.fieldDefaultValue,
            'fieldOrder': self.fieldOrder,
            'fieldSortOrder': self.fieldSortOrder,
            'fieldEnumId': self.fieldEnumId,
            'fieldFromUserProperties': self.fieldFromUserProperties,
            'isPrimaryKey': self.isPrimaryKey,
            'isNullable': self.isNullable,
            'isOrderField': self.isOrderField,
            'fieldPaint': self.fieldPaint,
            'fieldSource': self.fieldSource,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'createUserId': self.createUserId,
            'createUserName': self.createUserName,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataFieldDTO':
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
            tableId=data.get('tableId'),
            fieldName=data.get('fieldName'),
            fieldTitleName=data.get('fieldTitleName'),
            fieldAlia=data.get('fieldAlia'),
            fieldType=data.get('fieldType'),
            fieldDataType=data.get('fieldDataType'),
            fieldGeometryType=data.get('fieldGeometryType'),
            fieldDataLength=data.get('fieldDataLength'),
            fieldDefaultValue=data.get('fieldDefaultValue'),
            fieldOrder=data.get('fieldOrder'),
            fieldSortOrder=data.get('fieldSortOrder'),
            fieldEnumId=data.get('fieldEnumId'),
            fieldFromUserProperties=data.get('fieldFromUserProperties'),
            isPrimaryKey=data.get('isPrimaryKey'),
            isNullable=data.get('isNullable'),
            isOrderField=data.get('isOrderField'),
            fieldPaint=data.get('fieldPaint'),
            fieldSource=data.get('fieldSource'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            createUserId=data.get('createUserId'),
            createUserName=data.get('createUserName'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['MetadataFieldDTO']
