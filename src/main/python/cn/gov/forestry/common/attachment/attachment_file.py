from dataclasses import dataclass
from typing import Optional, Dict, Any


@dataclass
class AttachmentFile:
    uid: Optional[str] = None
    etag: Optional[str] = None
    name: Optional[str] = None
    extName: Optional[str] = None
    originName: Optional[str] = None
    url: Optional[str] = None
    thumbUrl: Optional[str] = None
    absoluteUrl: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        return {
            'uid': self.uid,
            'etag': self.etag,
            'name': self.name,
            'extName': self.extName,
            'originName': self.originName,
            'url': self.url,
            'thumbUrl': self.thumbUrl,
            'absoluteUrl': self.absoluteUrl,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'AttachmentFile':
        if data is None:
            return None
        return cls(
            uid=data.get('uid'),
            etag=data.get('etag'),
            name=data.get('name'),
            extName=data.get('extName'),
            originName=data.get('originName'),
            url=data.get('url'),
            thumbUrl=data.get('thumbUrl'),
            absoluteUrl=data.get('absoluteUrl'),
        )


__all__ = ['AttachmentFile']
