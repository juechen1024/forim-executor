package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import cn.gov.forestry.common.geojson.geometry.LineString;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LineStringFeature {
    private Object id;
    private String type = GeoJsonTypeEnum.FEATURE.getTypeName();
    private LineString geometry;
    private Map<String, Object> properties;
    private List<Double> bbox;
}
