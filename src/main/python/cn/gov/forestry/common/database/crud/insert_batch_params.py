from dataclasses import dataclass
from typing import List, Dict, Any, Optional

from cn.gov.forestry.common.database.database_info import DatabaseInfo
from cn.gov.forestry.common.database.field_value import FieldValue


@dataclass
class InsertBatchParams:
    databaseInfo: Optional[DatabaseInfo] = None
    tableEntityName: Optional[str] = None
    # propertiesList: List[Dict[str, FieldValue]]
    propertiesList: Optional[List[Dict[str, FieldValue]]] = None

    def to_dict(self) -> Dict[str, Any]:
        return {
            'databaseInfo': self.databaseInfo.to_dict() if self.databaseInfo else None,
            'tableEntityName': self.tableEntityName,
            'propertiesList': [
                {k: v.to_dict() if hasattr(v, 'to_dict') else v for k, v in prop.items()}
                for prop in (self.propertiesList or [])
            ],
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'InsertBatchParams':
        if data is None:
            return None
        db_info = DatabaseInfo.from_dict(data.get('databaseInfo'))
        props = []
        for p in data.get('propertiesList') or []:
            d = {}
            for k, v in p.items():
                d[k] = FieldValue.from_dict(v) if isinstance(v, dict) else v
            props.append(d)
        return cls(
            databaseInfo=db_info,
            tableEntityName=data.get('tableEntityName'),
            propertiesList=props,
        )


__all__ = ['InsertBatchParams']
