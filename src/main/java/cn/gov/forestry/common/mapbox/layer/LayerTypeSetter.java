package cn.gov.forestry.common.mapbox.layer;

public interface LayerTypeSetter {
    LayerAfterTypeSetter type(String type);
    LayerAfterTypeSetter backgroundLayer();
    LayerAfterTypeSetter circleLayer();
    LayerAfterTypeSetter clipLayer();
    LayerAfterTypeSetter fillExtrusionLayer();
    LayerAfterTypeSetter heatmapLayer();
    LayerAfterTypeSetter hillshadeLayer();
    LayerAfterTypeSetter fillLayer();
    LayerAfterTypeSetter lineLayer();
    LayerAfterTypeSetter modelLayer();
    LayerAfterTypeSetter rasterLayer();
    LayerAfterTypeSetter rasterParticleLayer();
    LayerAfterTypeSetter skyLayer();
    LayerAfterTypeSetter slotLayer();
    LayerAfterTypeSetter symbolLayer();
    LayerAfterTypeSetter unknownLayer();
}
