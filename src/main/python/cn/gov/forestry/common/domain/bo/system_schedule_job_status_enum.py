from enum import Enum
from typing import Optional


class SystemScheduleJobStatusEnum(Enum):
    INIT = ("0", "初始化", "normal")
    RUNNING = ("10", "运行中", "active")
    SUCCESS = ("100", "完成", "success")
    ERROR = ("500", "失败", "exception")

    def __init__(self, code: str, name: str, process_name: str):
        self.code = code
        self.name = name
        self.process_name = process_name

    @classmethod
    def getByCode(cls, code: Optional[str]) -> Optional["SystemScheduleJobStatusEnum"]:
        if code is None:
            return None
        for item in cls:
            if item.code == code:
                return item
        return None

    @classmethod
    def isRunning(cls, code: Optional[str]) -> bool:
        return code == cls.RUNNING.code


__all__ = ["SystemScheduleJobStatusEnum"]
