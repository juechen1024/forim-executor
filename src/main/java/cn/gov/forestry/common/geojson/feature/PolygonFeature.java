package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import cn.gov.forestry.common.geojson.geometry.Polygon;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PolygonFeature {
    private Object id;
    private String type = GeoJsonTypeEnum.FEATURE.getTypeName();
    private Polygon geometry;
    private Map<String, Object> properties;
    private List<Double> bbox;
}
