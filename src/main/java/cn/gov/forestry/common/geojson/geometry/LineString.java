package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LineString extends Geometry {
    private String type = GeoJsonTypeEnum.LINE_STRING.getTypeName();
    private List<List<Double>> coordinates;
}
