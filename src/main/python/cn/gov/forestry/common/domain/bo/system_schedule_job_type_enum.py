from enum import Enum
from typing import Optional


class SystemScheduleJobTypeEnum(Enum):
    IMPORT_EXCEL = ("201", "导入excel")
    IMPORT_SHAPEFILE = ("202", "导入shapefile")
    IMPORT_GEOTIFF = ("203", "导入geoTiff")
    TRANSFER_GLYPHS = ("301", "转换glyphs")

    def __init__(self, code: str, name: str):
        self.code = code
        self.name = name

    @classmethod
    def isImportExcel(cls, code: Optional[str]) -> bool:
        return code == cls.IMPORT_EXCEL.code

    @classmethod
    def isImportShapefile(cls, code: Optional[str]) -> bool:
        return code == cls.IMPORT_SHAPEFILE.code

    @classmethod
    def isImportGeoTiff(cls, code: Optional[str]) -> bool:
        return code == cls.IMPORT_GEOTIFF.code

    @classmethod
    def getByCode(cls, code: Optional[str]) -> Optional["SystemScheduleJobTypeEnum"]:
        if code is None:
            return None
        for item in cls:
            if item.code == code:
                return item
        return None


__all__ = ["SystemScheduleJobTypeEnum"]
