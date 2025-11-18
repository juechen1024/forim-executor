from dataclasses import dataclass
from typing import Optional, Dict, Any


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


__all__ = ['DatabaseInfo']
