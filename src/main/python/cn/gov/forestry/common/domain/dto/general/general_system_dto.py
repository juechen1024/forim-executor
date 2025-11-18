from dataclasses import dataclass
from typing import Optional, List, Dict, Any
from datetime import datetime

from cn.gov.forestry.common.attachment.attachment_upload_file import AttachmentUploadFile


@dataclass
class GeneralSystemDTO:
    id: Optional[str] = None
    systemName: Optional[str] = None
    systemLiteName: Optional[str] = None
    systemDescription: Optional[str] = None
    systemType: Optional[str] = None
    systemUrl: Optional[str] = None
    systemAssetsBucket: Optional[str] = None
    systemContent: Optional[str] = None
    systemAvatarFiles: Optional[List[AttachmentUploadFile]] = None
    systemIconFiles: Optional[List[AttachmentUploadFile]] = None
    systemCoverImageFiles: Optional[List[AttachmentUploadFile]] = None
    systemVersion: Optional[str] = None
    systemDatabaseType: Optional[str] = None
    systemDatabaseHost: Optional[str] = None
    systemDatabasePort: Optional[str] = None
    systemDatabaseUsername: Optional[str] = None
    systemDatabasePassword: Optional[str] = None
    systemDatabaseName: Optional[str] = None
    createTime: Optional[datetime] = None
    updateTime: Optional[datetime] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'id': self.id,
            'systemName': self.systemName,
            'systemLiteName': self.systemLiteName,
            'systemDescription': self.systemDescription,
            'systemType': self.systemType,
            'systemUrl': self.systemUrl,
            'systemAssetsBucket': self.systemAssetsBucket,
            'systemContent': self.systemContent,
            'systemAvatarFiles': [f.to_dict() for f in (self.systemAvatarFiles or [])],
            'systemIconFiles': [f.to_dict() for f in (self.systemIconFiles or [])],
            'systemCoverImageFiles': [f.to_dict() for f in (self.systemCoverImageFiles or [])],
            'systemVersion': self.systemVersion,
            'systemDatabaseType': self.systemDatabaseType,
            'systemDatabaseHost': self.systemDatabaseHost,
            'systemDatabasePort': self.systemDatabasePort,
            'systemDatabaseUsername': self.systemDatabaseUsername,
            'systemDatabasePassword': self.systemDatabasePassword,
            'systemDatabaseName': self.systemDatabaseName,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'GeneralSystemDTO':
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

        def parse_list_of_uploads(lst):
            return [AttachmentUploadFile.from_dict(x) for x in (lst or [])]

        return cls(
            id=data.get('id'),
            systemName=data.get('systemName'),
            systemLiteName=data.get('systemLiteName'),
            systemDescription=data.get('systemDescription'),
            systemType=data.get('systemType'),
            systemUrl=data.get('systemUrl'),
            systemAssetsBucket=data.get('systemAssetsBucket'),
            systemContent=data.get('systemContent'),
            systemAvatarFiles=parse_list_of_uploads(data.get('systemAvatarFiles')),
            systemIconFiles=parse_list_of_uploads(data.get('systemIconFiles')),
            systemCoverImageFiles=parse_list_of_uploads(data.get('systemCoverImageFiles')),
            systemVersion=data.get('systemVersion'),
            systemDatabaseType=data.get('systemDatabaseType'),
            systemDatabaseHost=data.get('systemDatabaseHost'),
            systemDatabasePort=data.get('systemDatabasePort'),
            systemDatabaseUsername=data.get('systemDatabaseUsername'),
            systemDatabasePassword=data.get('systemDatabasePassword'),
            systemDatabaseName=data.get('systemDatabaseName'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['GeneralSystemDTO']
