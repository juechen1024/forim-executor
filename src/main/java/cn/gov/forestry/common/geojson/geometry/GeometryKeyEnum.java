package cn.gov.forestry.common.geojson.geometry;

import lombok.Getter;

public enum GeometryKeyEnum {
    TYPE("type"),
    COORDINATES("coordinates");

    @Getter
    private String key;

    GeometryKeyEnum(String key) {
        this.key = key;
    }

    public static Boolean isGeometryKey(String key) {
        for (GeometryKeyEnum featureKeyEnum : GeometryKeyEnum.values()) {
            if (featureKeyEnum.getKey().equals(key)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
