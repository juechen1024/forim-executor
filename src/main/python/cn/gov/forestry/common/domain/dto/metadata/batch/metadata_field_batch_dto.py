from dataclasses import dataclass
from typing import Optional, List, Dict, Any

from cn.gov.forestry.common.domain.dto.metadata.metadata_field_dto import MetadataFieldDTO


@dataclass
class MetadataFieldBatchDTO:
    systemId: Optional[str] = None
    systemModule: Optional[str] = None
    tableId: Optional[str] = None
    fields: Optional[List[MetadataFieldDTO]] = None
    createdCount: int = 0

    def to_dict(self) -> Dict[str, Any]:
        return {
            'systemId': self.systemId,
            'systemModule': self.systemModule,
            'tableId': self.tableId,
            'fields': [f.to_dict() for f in (self.fields or [])],
            'createdCount': self.createdCount,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'MetadataFieldBatchDTO':
        if data is None:
            return None
        fields = [MetadataFieldDTO.from_dict(f) for f in (data.get('fields') or [])]
        return cls(
            systemId=data.get('systemId'),
            systemModule=data.get('systemModule'),
            tableId=data.get('tableId'),
            fields=fields,
            createdCount=data.get('createdCount', 0),
        )


__all__ = ['MetadataFieldBatchDTO']
