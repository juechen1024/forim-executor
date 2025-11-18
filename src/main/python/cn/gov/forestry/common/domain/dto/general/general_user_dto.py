from dataclasses import dataclass
from typing import Optional, List, Dict, Any
from datetime import datetime

from cn.gov.forestry.common.domain.dto.general.general_system_dto import GeneralSystemDTO
from cn.gov.forestry.common.domain.dto.general.general_system_dto import AttachmentUploadFile


@dataclass
class GeneralUserDTO:
    systemId: Optional[str] = None
    id: Optional[str] = None
    userName: Optional[str] = None
    userAlia: Optional[str] = None
    userPassword: Optional[str] = None
    userPhone: Optional[str] = None
    userEmail: Optional[str] = None
    userRealName: Optional[str] = None
    userLocal: Optional[str] = None
    userLastLoginTime: Optional[datetime] = None
    userCurrentToken: Optional[str] = None
    userRealmTypeCode: Optional[int] = None
    userRealmTypeName: Optional[str] = None
    userAvatarUrl: Optional[str] = None
    loginEnabled: Optional[bool] = None
    applyReset: Optional[bool] = None
    userRoleList: Optional[List[Dict[str, Any]]] = None
    userRole: Optional[List[str]] = None
    isBuildIn: Optional[bool] = None
    createTime: Optional[datetime] = None
    updateTime: Optional[datetime] = None
    userOrder: Optional[int] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'systemId': self.systemId,
            'id': self.id,
            'userName': self.userName,
            'userAlia': self.userAlia,
            'userPassword': self.userPassword,
            'userPhone': self.userPhone,
            'userEmail': self.userEmail,
            'userRealName': self.userRealName,
            'userLocal': self.userLocal,
            'userLastLoginTime': dt_to_iso(self.userLastLoginTime),
            'userCurrentToken': self.userCurrentToken,
            'userRealmTypeCode': self.userRealmTypeCode,
            'userRealmTypeName': self.userRealmTypeName,
            'userAvatarUrl': self.userAvatarUrl,
            'loginEnabled': self.loginEnabled,
            'applyReset': self.applyReset,
            'userRoleList': self.userRoleList,
            'userRole': self.userRole,
            'isBuildIn': self.isBuildIn,
            'createTime': dt_to_iso(self.createTime),
            'updateTime': dt_to_iso(self.updateTime),
            'userOrder': self.userOrder,
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'GeneralUserDTO':
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
            id=data.get('id'),
            userName=data.get('userName'),
            userAlia=data.get('userAlia'),
            userPassword=data.get('userPassword'),
            userPhone=data.get('userPhone'),
            userEmail=data.get('userEmail'),
            userRealName=data.get('userRealName'),
            userLocal=data.get('userLocal'),
            userLastLoginTime=parse_dt(data.get('userLastLoginTime')),
            userCurrentToken=data.get('userCurrentToken'),
            userRealmTypeCode=data.get('userRealmTypeCode'),
            userRealmTypeName=data.get('userRealmTypeName'),
            userAvatarUrl=data.get('userAvatarUrl'),
            loginEnabled=data.get('loginEnabled'),
            applyReset=data.get('applyReset'),
            userRoleList=data.get('userRoleList'),
            userRole=data.get('userRole'),
            isBuildIn=data.get('isBuildIn'),
            createTime=parse_dt(data.get('createTime')),
            updateTime=parse_dt(data.get('updateTime')),
            userOrder=data.get('userOrder'),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['GeneralUserDTO']
