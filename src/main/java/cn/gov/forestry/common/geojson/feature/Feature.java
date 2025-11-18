package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.geojson.GeoJsonTypeEnum;
import cn.gov.forestry.common.geojson.geometry.Geometry;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *  @id string,number
 *  @type string
 *  @geometry
 * {
 *     "type": "MultiPolygon",
 *     "coordinates": []
 * }
 *
 *  @properties
 * {
 *     "key": "value",
 *     ...
 * }
 *  @bbox [double,double,double,double] -
 * minx,miny,maxx,maxy -
 * minLng,minLat,maxLng,maxLat
 * */
@Data
public class Feature<T extends Geometry> {
    private Object id;
    private String type = GeoJsonTypeEnum.FEATURE.getTypeName();
    private T geometry;
    private Map<String, Object> properties;
    private List<Double> bbox;
}
