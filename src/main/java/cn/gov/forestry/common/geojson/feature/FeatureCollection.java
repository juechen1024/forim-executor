package cn.gov.forestry.common.geojson.feature;

import cn.gov.forestry.common.geojson.geometry.Geometry;
import lombok.Data;

import java.util.List;

@Data
public class FeatureCollection<T extends Geometry> {
    private String type = "FeatureCollection";
    private List<Feature<T>> features;
}
