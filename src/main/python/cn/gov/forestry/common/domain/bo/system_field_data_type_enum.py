from enum import Enum
from typing import Optional


class SystemFieldDataTypeEnum(Enum):
    INTEGER = ("integer", "整型", "Integer", "number")
    DOUBLE = ("double", "浮点型", "Double", "number")
    BOOLEAN = ("boolean", "布尔值", "Boolean", "boolean")
    STRING = ("string", "字符串", "String", "string")
    DATE = ("date", "时间类型", "Date", "string")
    OBJECT = ("object", "对象", "Document", "object")
    ARRAY = ("array", "对象数组", "Array", "object[]")

    def __init__(self, code: str, label: str, db_data_type: str, ts_data_type: str):
        self.code = code
        self.label = label
        self.dbDataType = db_data_type
        self.tsDataType = ts_data_type

    @classmethod
    def getByCodeOrDefault(cls, code: Optional[str]) -> "SystemFieldDataTypeEnum":
        if code is None:
            return cls.STRING
        for item in cls:
            if item.code == code:
                return item
        return cls.STRING


__all__ = ["SystemFieldDataTypeEnum"]
