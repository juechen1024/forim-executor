package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class GeometryCollection {
    private String type = GeoJsonTypeEnum.GEOMETRY_COLLECTION.getTypeName();
    private List<Geometry> geometries;
}
