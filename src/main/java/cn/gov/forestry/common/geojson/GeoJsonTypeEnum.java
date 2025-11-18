package cn.gov.forestry.common.geojson;

import lombok.Getter;

public enum GeoJsonTypeEnum {

    FEATURE_COLLECTION("FeatureCollection"),

    FEATURE("Feature"),

    GEOMETRY_COLLECTION("GeometryCollection"),

    GEOMETRY("Geometry"),

    POINT("Point"),

    MULTI_POINT("MultiPoint"),

    LINE_STRING("LineString"),

    MULTI_LINE_STRING("MultiLineString"),

    POLYGON("Polygon"),

    MULTI_POLYGON("MultiPolygon")
    ;

    @Getter
    private final String typeName;

    GeoJsonTypeEnum(String typeName) {
        this.typeName = typeName;
    }
}
