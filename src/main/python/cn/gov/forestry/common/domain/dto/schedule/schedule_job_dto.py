from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class ScheduleJobDTO:
    systemId: Optional[str] = None
    id: Optional[str] = None
    jobCreateUserId: Optional[str] = None
    jobCreateUserName: Optional[str] = None
    jobCreateTime: Optional[datetime] = None
    jobType: Optional[str] = None
    jobTaskCount: Optional[int] = None
    jobTaskOffset: Optional[int] = None
    jobStatus: Optional[str] = None
    jobMaxRetryTimes: Optional[int] = None
    jobCurrentRetryTimes: Optional[int] = None
    jobParams: Optional[Dict[str, Any]] = None
    jobResult: Optional[Dict[str, Any]] = None
    jobOrder: Optional[int] = None
    jobStartTime: Optional[datetime] = None
    jobEndTime: Optional[datetime] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'systemId': self.systemId,
            'id': self.id,
            'jobCreateUserId': self.jobCreateUserId,
            'jobCreateUserName': self.jobCreateUserName,
            'jobCreateTime': dt_to_iso(self.jobCreateTime),
            'jobType': self.jobType,
            'jobTaskCount': self.jobTaskCount,
            'jobTaskOffset': self.jobTaskOffset,
            'jobStatus': self.jobStatus,
            'jobMaxRetryTimes': self.jobMaxRetryTimes,
            'jobCurrentRetryTimes': self.jobCurrentRetryTimes,
            'jobParams': self.jobParams,
            'jobResult': self.jobResult,
            'jobOrder': self.jobOrder,
            'jobStartTime': dt_to_iso(self.jobStartTime),
            'jobEndTime': dt_to_iso(self.jobEndTime),
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'ScheduleJobDTO':
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
            jobCreateUserId=data.get('jobCreateUserId'),
            jobCreateUserName=data.get('jobCreateUserName'),
            jobCreateTime=parse_dt(data.get('jobCreateTime')),
            jobType=data.get('jobType'),
            jobTaskCount=data.get('jobTaskCount'),
            jobTaskOffset=data.get('jobTaskOffset'),
            jobStatus=data.get('jobStatus'),
            jobMaxRetryTimes=data.get('jobMaxRetryTimes'),
            jobCurrentRetryTimes=data.get('jobCurrentRetryTimes'),
            jobParams=data.get('jobParams'),
            jobResult=data.get('jobResult'),
            jobOrder=data.get('jobOrder'),
            jobStartTime=parse_dt(data.get('jobStartTime')),
            jobEndTime=parse_dt(data.get('jobEndTime')),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['ScheduleJobDTO']
