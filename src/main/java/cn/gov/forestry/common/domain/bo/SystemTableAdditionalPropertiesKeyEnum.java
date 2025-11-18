package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemTableAdditionalPropertiesKeyEnum {
    SHAPE_FILE_METADATA_PROPERTIES("shapeFileMetadataProperties"),
    TIF_METADATA_PROPERTIES("tifMetadataProperties"),
    VECTOR_TILES_CONFIG("vectorTilesConfig"),
    RASTER_TILES_CONFIG("rasterTilesConfig"),
    STORAGE_CONFIG("storageConfig"),
    SCHEDULE_CONFIG("scheduleConfig"),
    PERMISSION_CONFIG("permissionConfig"),
    ;
    @Getter
    private String key;

    SystemTableAdditionalPropertiesKeyEnum(String key) {
        this.key = key;
    }
}
