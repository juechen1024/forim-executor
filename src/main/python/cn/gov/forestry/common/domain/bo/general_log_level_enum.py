from enum import Enum


class GeneralLogLevelEnum(Enum):
    DEBUG = ("debug", 100)
    INFO = ("info", 200)
    ERROR = ("error", 500)

    def __init__(self, code: str, level: int):
        self.code = code
        self.level = level


__all__ = ["GeneralLogLevelEnum"]
