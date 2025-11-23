from dataclasses import dataclass
from typing import Optional, Dict, Any, TYPE_CHECKING

if TYPE_CHECKING:
    from cn.gov.forestry.common.domain.dto.general.general_system_dto import GeneralSystemDTO


@dataclass
class DatabaseInfo:
    systemId: Optional[str] = None
    databaseType: Optional[str] = None
    databaseHost: Optional[str] = None
    databasePort: Optional[str] = None
    databaseUsername: Optional[str] = None
    databasePassword: Optional[str] = None
    databaseName: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        return {
            'systemId': self.systemId,
            'databaseType': self.databaseType,
            'databaseHost': self.databaseHost,
            'databasePort': self.databasePort,
            'databaseUsername': self.databaseUsername,
            'databasePassword': self.databasePassword,
            'databaseName': self.databaseName,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'DatabaseInfo':
        if data is None:
            return None
        return cls(
            systemId=data.get('systemId'),
            databaseType=data.get('databaseType'),
            databaseHost=data.get('databaseHost'),
            databasePort=data.get('databasePort'),
            databaseUsername=data.get('databaseUsername'),
            databasePassword=data.get('databasePassword'),
            databaseName=data.get('databaseName'),
        )

    @classmethod
    def from_system_info(cls, system: 'GeneralSystemDTO') -> 'DatabaseInfo':
        if system is None:
            return cls()
        return cls(
            systemId=getattr(system, 'id', None),
            databaseType=getattr(system, 'systemDatabaseType', None),
            databaseHost=getattr(system, 'systemDatabaseHost', None),
            databasePort=getattr(system, 'systemDatabasePort', None),
            databaseUsername=getattr(system, 'systemDatabaseUsername', None),
            databasePassword=getattr(system, 'systemDatabasePassword', None),
            databaseName=getattr(system, 'systemDatabaseName', None),
        )


__all__ = ['DatabaseInfo']
