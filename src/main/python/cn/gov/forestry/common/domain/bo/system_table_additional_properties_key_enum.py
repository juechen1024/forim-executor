from enum import Enum


class SystemTableAdditionalPropertiesKeyEnum(Enum):
    SHAPE_FILE_METADATA_PROPERTIES = "shapeFileMetadataProperties"
    TIF_METADATA_PROPERTIES = "tifMetadataProperties"
    VECTOR_TILES_CONFIG = "vectorTilesConfig"
    RASTER_TILES_CONFIG = "rasterTilesConfig"
    STORAGE_CONFIG = "storageConfig"
    SCHEDULE_CONFIG = "scheduleConfig"
    PERMISSION_CONFIG = "permissionConfig"

    @property
    def key(self) -> str:
        return self.value


__all__ = ["SystemTableAdditionalPropertiesKeyEnum"]
