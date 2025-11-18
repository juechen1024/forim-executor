from dataclasses import dataclass
from typing import Optional, Dict, Any
from datetime import datetime


@dataclass
class ScheduleJobLogDTO:
    systemId: Optional[str] = None
    jobId: Optional[str] = None
    jobType: Optional[str] = None
    jobExecutor: Optional[str] = None
    jobLogLevel: Optional[int] = None
    jobLogTime: Optional[datetime] = None
    jobLogContent: Optional[str] = None
    createTime: Optional[datetime] = None
    additionalProperties: Optional[Dict[str, Any]] = None

    def to_dict(self) -> Dict[str, Any]:
        def dt_to_iso(v):
            return v.isoformat() if isinstance(v, datetime) else v

        return {
            'systemId': self.systemId,
            'jobId': self.jobId,
            'jobType': self.jobType,
            'jobExecutor': self.jobExecutor,
            'jobLogLevel': self.jobLogLevel,
            'jobLogTime': dt_to_iso(self.jobLogTime),
            'jobLogContent': self.jobLogContent,
            'createTime': dt_to_iso(self.createTime),
            'additionalProperties': self.additionalProperties,
        }

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'ScheduleJobLogDTO':
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
            jobId=data.get('jobId'),
            jobType=data.get('jobType'),
            jobExecutor=data.get('jobExecutor'),
            jobLogLevel=data.get('jobLogLevel'),
            jobLogTime=parse_dt(data.get('jobLogTime')),
            jobLogContent=data.get('jobLogContent'),
            createTime=parse_dt(data.get('createTime')),
            additionalProperties=data.get('additionalProperties'),
        )


__all__ = ['ScheduleJobLogDTO']
