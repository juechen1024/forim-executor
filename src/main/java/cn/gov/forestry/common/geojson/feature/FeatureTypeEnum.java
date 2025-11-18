package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Getter;

public enum FeatureTypeEnum {
    POINT_FEATURE(GeoJsonTypeEnum.POINT.getTypeName(), PointFeature.class),
    MULTI_POINT_FEATURE(GeoJsonTypeEnum.MULTI_POINT.getTypeName(), MultiPointFeature.class),
    LINE_STRING_FEATURE(GeoJsonTypeEnum.LINE_STRING.getTypeName(), LineStringFeature.class),
    MULTI_LINE_STRING_FEATURE(GeoJsonTypeEnum.MULTI_LINE_STRING.getTypeName(), MultiLineStringFeature.class),
    POLYGON_FEATURE(GeoJsonTypeEnum.POLYGON.getTypeName(), PolygonFeature.class),
    MULTI_POLYGON_FEATURE(GeoJsonTypeEnum.MULTI_POLYGON.getTypeName(), MultiPolygonFeature.class),
    ;
    @Getter
    private final String typeName;
    @Getter
    private final Class<?> clazz;

    FeatureTypeEnum(String typeName, Class<?> clazz) {
        this.typeName = typeName;
        this.clazz = clazz;
    }

    public static FeatureTypeEnum byTypeName(String typeName) {
        for (FeatureTypeEnum value : FeatureTypeEnum.values()) {
            if (value.typeName.equals(typeName)) {
                return value;
            }
        }
        return null;
    }
    // jts typeName demo: org.locationtech.jts.geom.MultiPolygon
    public static FeatureTypeEnum byJtsTypeName(String jtsTypeName) {
        for (FeatureTypeEnum value : FeatureTypeEnum.values()) {
            String compareJtsTypeName = "org.locationtech.jts.geom." + value.typeName;
            if (compareJtsTypeName.equals(jtsTypeName)) {
                return value;
            }
        }
        return null;
    }
}
