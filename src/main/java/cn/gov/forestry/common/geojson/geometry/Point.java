package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Point extends Geometry {
    private String type = GeoJsonTypeEnum.POINT.getTypeName();
    private List<Double> coordinates;
}
