package cn.gov.forestry.common.mapbox.layer;

public interface LayerAfterTypeSetter {
    LayerAfterTypeSetter id(String id);
    LayerSpecification build();
}
