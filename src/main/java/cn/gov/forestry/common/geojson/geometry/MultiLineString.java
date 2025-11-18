package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MultiLineString extends Geometry {
    private String type = GeoJsonTypeEnum.MULTI_LINE_STRING.getTypeName();
    private List<List<List<Double>>> coordinates;
}
