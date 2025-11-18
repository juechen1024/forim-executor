from dataclasses import dataclass
from typing import Any, Optional, Dict

from cn.gov.forestry.common.attachment.attachment_file import AttachmentFile


@dataclass
class AttachmentUploadFile:
    uid: Optional[str] = None
    lastModified: Optional[Any] = None
    lastModifiedDate: Optional[str] = None
    name: Optional[str] = None
    size: Optional[Any] = None
    type: Optional[str] = None
    percent: Optional[Any] = None
    originFileObj: Optional[Any] = None
    status: Optional[str] = None
    response: Optional[AttachmentFile] = None

    def to_dict(self) -> Dict:
        return {
            'uid': self.uid,
            'lastModified': self.lastModified,
            'lastModifiedDate': self.lastModifiedDate,
            'name': self.name,
            'size': self.size,
            'type': self.type,
            'percent': self.percent,
            'originFileObj': self.originFileObj,
            'status': self.status,
            'response': self.response.to_dict() if self.response else None,
        }

    @classmethod
    def from_dict(cls, data: Dict) -> 'AttachmentUploadFile':
        if data is None:
            return None
        resp = AttachmentFile.from_dict(data.get('response')) if data.get('response') else None
        return cls(
            uid=data.get('uid'),
            lastModified=data.get('lastModified'),
            lastModifiedDate=data.get('lastModifiedDate'),
            name=data.get('name'),
            size=data.get('size'),
            type=data.get('type'),
            percent=data.get('percent'),
            originFileObj=data.get('originFileObj'),
            status=data.get('status'),
            response=resp,
        )


__all__ = ['AttachmentUploadFile']
