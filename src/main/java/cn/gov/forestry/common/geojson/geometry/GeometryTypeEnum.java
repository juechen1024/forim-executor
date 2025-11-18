package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

public enum GeometryTypeEnum {
    POINT(GeoJsonTypeEnum.POINT.getTypeName(), Point.class),
    MULTI_POINT(GeoJsonTypeEnum.MULTI_POINT.getTypeName(), MultiPoint.class),
    LINE_STRING(GeoJsonTypeEnum.LINE_STRING.getTypeName(), LineString.class),
    MULTI_LINE_STRING(GeoJsonTypeEnum.MULTI_LINE_STRING.getTypeName(), MultiLineString.class),
    POLYGON(GeoJsonTypeEnum.POLYGON.getTypeName(), Polygon.class),
    MULTI_POLYGON(GeoJsonTypeEnum.MULTI_POLYGON.getTypeName(), MultiPolygon.class),
    GEOMETRY_COLLECTION(GeoJsonTypeEnum.GEOMETRY_COLLECTION.getTypeName(), GeometryCollection.class),
    ;
    @Getter
    private final String typeName;
    @Getter
    private final Class<?> clazz;

    GeometryTypeEnum(String typeName, Class<?> clazz) {
        this.typeName = typeName;
        this.clazz = clazz;
    }

    public static GeometryTypeEnum byTypeName(String typeName) {
        for (GeometryTypeEnum value : GeometryTypeEnum.values()) {
            if (value.typeName.equals(typeName)) {
                return value;
            }
        }
        return null;
    }
    // jts typeName demo: org.locationtech.jts.geom.MultiPolygon
    public static GeometryTypeEnum byJtsTypeName(String jtsTypeName) {
        for (GeometryTypeEnum value : GeometryTypeEnum.values()) {
            String compareJtsTypeName = "org.locationtech.jts.geom." + value.typeName;
            if (compareJtsTypeName.equals(jtsTypeName)) {
                return value;
            }
        }
        return null;
    }

    public static Object createInstanceByTypeName(String typeName) {
        GeometryTypeEnum geometryType = GeometryTypeEnum.byTypeName(typeName);
        if (geometryType == null) {
            throw new IllegalArgumentException("Unknown geometry type name: " + typeName);
        }

        try {
            // 使用反射调用无参构造方法创建实例
            return geometryType.getClazz().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance for geometry type: " + typeName, e);
        }
    }

    public static Boolean isPointType(String typeName) {
        return ObjectUtils.nullSafeEquals(GeometryTypeEnum.POINT.getTypeName(), typeName) || ObjectUtils.nullSafeEquals(GeometryTypeEnum.MULTI_POINT.getTypeName(), typeName);
    }
    public static Boolean isLineStringType(String typeName) {
        return ObjectUtils.nullSafeEquals(GeometryTypeEnum.LINE_STRING.getTypeName(), typeName) || ObjectUtils.nullSafeEquals(GeometryTypeEnum.MULTI_LINE_STRING.getTypeName(), typeName);
    }
    public static Boolean isPolygonType(String typeName) {
        return ObjectUtils.nullSafeEquals(GeometryTypeEnum.POLYGON.getTypeName(), typeName) || ObjectUtils.nullSafeEquals(GeometryTypeEnum.MULTI_POLYGON.getTypeName(), typeName);
    }
}
