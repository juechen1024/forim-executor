"""
Data class mirroring `cn.gov.forestry.common.file.FileContent` (Java).
Provides JSON (de)serialization with base64 encoding for `bytes` field.
"""
from dataclasses import dataclass, field, asdict
from typing import Optional, Dict, Any
import base64


@dataclass
class FileContent:
    systemId: Optional[str] = None
    name: Optional[str] = None
    originalFilename: Optional[str] = None
    contentType: Optional[str] = None
    contentLength: Optional[str] = None
    size: Optional[int] = None
    eTag: Optional[str] = None
    bytes: Optional[bytes] = None
    success: Optional[bool] = None
    message: Optional[str] = None
    uid: Optional[str] = None
    # 给innerResource专用的字段
    resourcePath: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        d = asdict(self)
        if self.bytes is not None:
            d['bytes'] = base64.b64encode(self.bytes).decode('ascii')
        else:
            d['bytes'] = None
        return d

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'FileContent':
        b = data.get('bytes')
        if b is not None:
            try:
                decoded = base64.b64decode(b)
            except Exception:
                decoded = None
        else:
            decoded = None

        return cls(
            systemId=data.get('systemId'),
            name=data.get('name'),
            originalFilename=data.get('originalFilename'),
            contentType=data.get('contentType'),
            contentLength=data.get('contentLength'),
            size=data.get('size'),
            eTag=data.get('eTag'),
            bytes=decoded,
            success=data.get('success'),
            message=data.get('message'),
            uid=data.get('uid'),
            resourcePath=data.get('resourcePath'),
        )


__all__ = ['FileContent']
