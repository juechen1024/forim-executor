package cn.gov.forestry.common.mapbox.layer;

import cn.gov.forestry.common.domain.dto.map.MapLayerDTO;
import cn.gov.forestry.common.domain.dto.metadata.MetadataTableDTO;
import cn.gov.forestry.common.geojson.geometry.GeometryTypeEnum;
import org.springframework.util.ObjectUtils;

import java.util.*;

public class LayerSpecificationBuilder implements LayerTypeSetter, LayerAfterTypeSetter {
    private final LayerSpecification layerSpecification = new LayerSpecification();

    private LayerSpecificationBuilder() {}

    public static LayerTypeSetter builder() {
        return new LayerSpecificationBuilder();
    }

    @Override
    public LayerAfterTypeSetter id(String id) {
        layerSpecification.setId(id);
        return this;
    }

    @Override
    public LayerAfterTypeSetter type(String type) {
        layerSpecification.setType(type);
        return this;
    }

    @Override
    public LayerSpecification build() {
        return layerSpecification;
    }

    @Override
    public LayerAfterTypeSetter backgroundLayer() {
        return this.type("background");
    }
    @Override
    public LayerAfterTypeSetter circleLayer() {
        return this.type("circle");
    }
    @Override
    public LayerAfterTypeSetter clipLayer() {
        return this.type("clip");
    }
    @Override
    public LayerAfterTypeSetter fillExtrusionLayer() {
        return this.type("fill-extrusion");
    }
    @Override
    public LayerAfterTypeSetter heatmapLayer() {
        return this.type("heatmap");
    }
    @Override
    public LayerAfterTypeSetter hillshadeLayer() {
        return this.type("hillshade");
    }
    @Override
    public LayerAfterTypeSetter fillLayer() {
        return this.type("fill");
    }
    @Override
    public LayerAfterTypeSetter lineLayer() {
        return this.type("line");
    }
    @Override
    public LayerAfterTypeSetter modelLayer() {
        return this.type("model");
    }
    @Override
    public LayerAfterTypeSetter rasterLayer() {
        return this.type("raster");
    }
    @Override
    public LayerAfterTypeSetter rasterParticleLayer() {
        return this.type("raster-particle");
    }
    @Override
    public LayerAfterTypeSetter skyLayer() {
        return this.type("sky");
    }
    @Override
    public LayerAfterTypeSetter slotLayer() {
        return this.type("slot");
    }
    @Override
    public LayerAfterTypeSetter symbolLayer() {
        return this.type("symbol");
    }
    @Override
    public LayerAfterTypeSetter unknownLayer() {
        return this.type("unknown");
    }


    public static LayerSpecification generateDefaultBackgroundLayer() {
        LayerSpecification defaultBackgroundLayer = LayerSpecificationBuilder.builder()
                .backgroundLayer()
                .id("background")
                .build();
        defaultBackgroundLayer.setMinZoom(0);
        Map<String, Object> backgroundPaint = new HashMap<>();
        backgroundPaint.put("background-color", "hsl(222, 56%, 4%)");
        backgroundPaint.put("background-opacity", 0.9);
        defaultBackgroundLayer.setPaint(backgroundPaint);
        Map<String, Object> layerMetadata = new HashMap<>();
        layerMetadata.put("layerName", "background");
        defaultBackgroundLayer.setMetadata(layerMetadata);
        return defaultBackgroundLayer;
    }

    public static List<LayerSpecification> generateTiandituBaseLayers() {
        List<LayerSpecification> layers = new ArrayList<>();

        // 每个图层的信息用 Map 来表示：id, source, visibility
        List<Map<String, String>> layerConfigs = Arrays.asList(
                createTiandituLayerConfig("tdtImgLayer", "tdtImgSource", "none"),
                createTiandituLayerConfig("tdtVecLayer", "tdtVecSource", "visible"),
                createTiandituLayerConfig("tdtTerLayer", "tdtTerSource", "none")
        );

        for (Map<String, String> config : layerConfigs) {
            LayerSpecification layer = LayerSpecificationBuilder.builder().rasterLayer().build();
            layer.setId(config.get("id"));
            layer.setSource(config.get("source"));
            layer.setType("raster");
            layer.setMinZoom(0);
            layer.setMaxZoom(22);

            Map<String, Object> layout = new HashMap<>();
            layout.put("visibility", config.get("visibility"));
            layer.setLayout(layout);

            Map<String, Object> layerMetadata = new HashMap<>();
            layerMetadata.put("layerName", config.get("id"));
            layer.setMetadata(layerMetadata);

            layers.add(layer);
        }

        return layers;
    }
    public static List<LayerSpecification> generateTiandituBaseSymbolLayers() {
        List<LayerSpecification> layers = new ArrayList<>();

        // 每个图层的信息用 Map 来表示：id, source, visibility
        List<Map<String, String>> layerConfigs = Arrays.asList(
                createTiandituLayerConfig("tdtCiaLayer", "tdtCiaSource", "none"),
                createTiandituLayerConfig("tdtCvaLayer", "tdtCvaSource", "visible"),
                createTiandituLayerConfig("tdtCtaLayer", "tdtCtaSource", "none")
        );

        for (Map<String, String> config : layerConfigs) {
            LayerSpecification layer = LayerSpecificationBuilder.builder().rasterLayer().build();
            layer.setId(config.get("id"));
            layer.setSource(config.get("source"));
            layer.setType("raster");
            layer.setMinZoom(0);
            layer.setMaxZoom(22);

            Map<String, Object> layout = new HashMap<>();
            layout.put("visibility", config.get("visibility"));
            layer.setLayout(layout);

            Map<String, Object> layerMetadata = new HashMap<>();
            layerMetadata.put("layerName", config.get("id"));
            layer.setMetadata(layerMetadata);

            layers.add(layer);
        }

        return layers;
    }
    private static Map<String, String> createTiandituLayerConfig(String id, String source, String visibility) {
        Map<String, String> config = new HashMap<>();
        config.put("id", id);
        config.put("source", source);
        config.put("visibility", visibility);
        return config;
    }

    public static LayerSpecification generateDefaultVectorLayer(MetadataTableDTO metadataTableDTO, String sourceKey) {
        if ( GeometryTypeEnum.isPolygonType(metadataTableDTO.getTableGeometryType())) {
            LayerSpecification layerSpecification = LayerSpecificationBuilder.builder().fillLayer().build();
            layerSpecification.setId(metadataTableDTO.getId());
            layerSpecification.setSource(sourceKey);
            layerSpecification.setSourceLayer(metadataTableDTO.getId());
            layerSpecification.setMinZoom(0);
            layerSpecification.setMaxZoom(22);

            Map<String, Object> fillLayout = new HashMap<>();
            fillLayout.put("visibility" ,"none");
            layerSpecification.setLayout(fillLayout);

            Map<String, Object> fillPaint = new HashMap<>();
            fillPaint.put("fill-color", "rgba(56,168,0,0.8)");
            layerSpecification.setPaint(fillPaint);
            layerSpecification.setMetadata(getLayerMetadata(metadataTableDTO));
            return layerSpecification;
        } else if (GeometryTypeEnum.isLineStringType(metadataTableDTO.getTableGeometryType())) {
            LayerSpecification layerSpecification = LayerSpecificationBuilder.builder().lineLayer().build();
            layerSpecification.setId(metadataTableDTO.getId());
            layerSpecification.setSource(sourceKey);
            layerSpecification.setSourceLayer(metadataTableDTO.getId());
            layerSpecification.setMinZoom(0);
            layerSpecification.setMaxZoom(22);

            Map<String, Object> lineLayout = new HashMap<>();
            lineLayout.put("visibility", "none");
            layerSpecification.setLayout(lineLayout);

            Map<String, Object> linePaint = new HashMap<>();
            linePaint.put("line-color", "rgba(56,168,0,0.8)");
            linePaint.put("line-width", 2);
            layerSpecification.setPaint(linePaint);
            layerSpecification.setMetadata(getLayerMetadata(metadataTableDTO));
            return layerSpecification;
        } else if (GeometryTypeEnum.isPointType(metadataTableDTO.getTableGeometryType())) {
            LayerSpecification layerSpecification = LayerSpecificationBuilder.builder().symbolLayer().build();
            layerSpecification.setId(metadataTableDTO.getId());
            layerSpecification.setSource(sourceKey);
            layerSpecification.setSourceLayer(metadataTableDTO.getId());
            layerSpecification.setMinZoom(0);
            layerSpecification.setMaxZoom(22);

            Map<String, Object> symbolLayout = new HashMap<>();
            symbolLayout.put("visibility", "none");
            symbolLayout.put("icon-image", "icons8-marker-48");
            symbolLayout.put("symbol-placement", "point");
            symbolLayout.put("text-pitch-alignment", "viewport");

            // 对于"text-field"属性，它似乎是一个混合类型的数组（字符串和对象）
            List<Object> textField = new ArrayList<>();
            textField.add("format");
            textField.add(Arrays.asList("get", "name"));
            textField.add(new HashMap<String, Double>() {{
                put("font-scale", 1.0);
            }});
            symbolLayout.put("text-field", textField);

            // 其他简单键值对可以直接添加
            symbolLayout.put("text-offset", new double[]{0, 0});
            symbolLayout.put("text-keep-upright", false);
            symbolLayout.put("text-rotation-alignment", "viewport");
            symbolLayout.put("text-anchor", "center");
            symbolLayout.put("text-size", 12);
            symbolLayout.put("text-padding", 2);
            symbolLayout.put("text-allow-overlap", true);
            symbolLayout.put("text-font", new String[]{"MicrosoftYaHei"});

            layerSpecification.setLayout(symbolLayout);

            Map<String, Object> symbolPaint = new HashMap<>();
            symbolPaint.put("text-color", "rgb(0,0,0)");
            symbolPaint.put("text-halo-color", "rgba(255, 255, 255, 0.78)");

            layerSpecification.setPaint(symbolPaint);
            layerSpecification.setMetadata(getLayerMetadata(metadataTableDTO));
            return layerSpecification;
        } else {
            return LayerSpecificationBuilder.builder().unknownLayer().build();
        }
    }
    public static LayerSpecification generateDefaultRasterLayer(MetadataTableDTO metadataTableDTO) {
        LayerSpecification layerSpecification = LayerSpecificationBuilder.builder().rasterLayer().build();
        layerSpecification.setId(metadataTableDTO.getId());
        layerSpecification.setSource(metadataTableDTO.getId());
        layerSpecification.setMinZoom(0);
        layerSpecification.setMaxZoom(22);
        Map<String, Object> rasterLayout = new HashMap<>();
        rasterLayout.put("visibility" ,"none");
        layerSpecification.setLayout(rasterLayout);
        layerSpecification.setMetadata(getLayerMetadata(metadataTableDTO));
        return layerSpecification;
    }

    public static MapLayerDTO convertToMapVectorLayerDTO(LayerSpecification layerSpecification, String systemId, String styleName) {
        MapLayerDTO layerDTO = new MapLayerDTO();
        layerDTO.setSystemId(systemId);
        layerDTO.setStyleName(styleName);
        layerDTO.setLayerId(layerSpecification.getId());
        layerDTO.setType(layerSpecification.getType());
        layerDTO.setMetadata(layerSpecification.getMetadata());
        layerDTO.setSource(layerSpecification.getSource());
        layerDTO.setSourceLayer(layerSpecification.getSourceLayer());
        layerDTO.setSlot(layerSpecification.getSlot());
        layerDTO.setMinZoom(layerSpecification.getMinZoom());
        layerDTO.setMaxZoom(layerSpecification.getMaxZoom());
        layerDTO.setFilter(layerSpecification.getFilter());
        layerDTO.setLayout(layerSpecification.getLayout());
        layerDTO.setPaint(layerSpecification.getPaint());
        layerDTO.setAdditionalProperties(layerSpecification.getAdditionalProperties());


        return layerDTO;
    }
    public static MapLayerDTO convertToMapRasterLayerDTO(LayerSpecification layerSpecification, String systemId, String styleName) {
        MapLayerDTO layerDTO = new MapLayerDTO();
        layerDTO.setSystemId(systemId);
        layerDTO.setStyleName(styleName);
        layerDTO.setLayerId(layerSpecification.getId());
        layerDTO.setType(layerSpecification.getType());
        layerDTO.setMetadata(layerSpecification.getMetadata());
        layerDTO.setSource(layerSpecification.getSource());
        layerDTO.setSlot(layerSpecification.getSlot());
        layerDTO.setMinZoom(layerSpecification.getMinZoom());
        layerDTO.setMaxZoom(layerSpecification.getMaxZoom());
        layerDTO.setLayout(layerSpecification.getLayout());
        layerDTO.setPaint(layerSpecification.getPaint());
        layerDTO.setAdditionalProperties(layerSpecification.getAdditionalProperties());


        return layerDTO;
    }

    public static MapLayerDTO convertToMapVectorLayerDTO(MetadataTableDTO metadataTableDTO, String styleName, String sourceKey) {
        LayerSpecification layerSpecification = generateDefaultVectorLayer(metadataTableDTO, sourceKey);
        return convertToMapVectorLayerDTO(layerSpecification, metadataTableDTO.getSystemId(), styleName);
    }
    public static MapLayerDTO convertToMapRasterLayerDTO(MetadataTableDTO metadataTableDTO, String styleName) {
        LayerSpecification layerSpecification = generateDefaultRasterLayer(metadataTableDTO);
        return convertToMapRasterLayerDTO(layerSpecification, metadataTableDTO.getSystemId(), styleName);
    }

    private static Map<String, Object> getLayerMetadata(MetadataTableDTO metadataTableDTO) {
        Map<String, Object> layerMetadata = metadataTableDTO.getAdditionalProperties();
        if (ObjectUtils.isEmpty(layerMetadata)) {
            layerMetadata = new HashMap<>();
        }
        layerMetadata.put("tableId", metadataTableDTO.getId());
        layerMetadata.put("layerName", metadataTableDTO.getTableTitleName());
        return layerMetadata;
    }
}
