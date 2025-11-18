from dataclasses import dataclass
from typing import Optional, List, Dict, Any

from cn.gov.forestry.common.rule.data_filter_rule import DataFilterRule


@dataclass
class MetadataTablePermissionDTO:
    id: Optional[str] = None
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    roleId: Optional[str] = None
    tableId: Optional[str] = None
    canQuery: Optional[bool] = None
    canCreate: Optional[bool] = None
    canModify: Optional[bool] = None
    canDelete: Optional[bool] = None
    canAlter: Optional[bool] = None
    canDrop: Optional[bool] = None
    canGrant: Optional[bool] = None
    dataFilterRules: Optional[List[Dict[str, Any]]] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        return {
            'id': self.id,
            'systemId': self.systemId,
            'systemModule': self.systemModule,
            'roleId': self.roleId,
            'tableId': self.tableId,
            'canQuery': self.canQuery,
            'canCreate': self.canCreate,
            'canModify': self.canModify,
            'canDelete': self.canDelete,
            'canAlter': self.canAlter,
            'canDrop': self.canDrop,
            'canGrant': self.canGrant,
            'dataFilterRules': self.dataFilterRules,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataTablePermissionDTO':
        if data is None:
            return None
        return cls(
            id=data.get('id'),
            systemId=data.get('systemId'),
            systemModule=data.get('systemModule'),
            roleId=data.get('roleId'),
            tableId=data.get('tableId'),
            canQuery=data.get('canQuery'),
            canCreate=data.get('canCreate'),
            canModify=data.get('canModify'),
            canDelete=data.get('canDelete'),
            canAlter=data.get('canAlter'),
            canDrop=data.get('canDrop'),
            canGrant=data.get('canGrant'),
            dataFilterRules=data.get('dataFilterRules'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['MetadataTablePermissionDTO']
