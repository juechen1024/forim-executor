package cn.gov.forestry.common.geojson.geometry;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MultiPoint extends Geometry {
    private String type = GeoJsonTypeEnum.MULTI_POINT.getTypeName();
    private List<List<Double>> coordinates;
}
