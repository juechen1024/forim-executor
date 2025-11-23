from enum import Enum


class SystemBuildInFieldEnum(Enum):
    ID = ("id", 1)
    GEOMETRY = ("geometry", 2)
    GEOMETRY_BBOX = ("geometry_bbox", 2)
    GEOMETRY_AREA = ("geometry_area", 2)
    ORDER = ("order", 3)

    def __init__(self, field_name: str, field_filter: int):
        self.fieldName = field_name
        self.fieldFilter = field_filter


__all__ = ["SystemBuildInFieldEnum"]
