package cn.gov.forestry.common.mapbox.source;

import java.util.Collections;
import java.util.HashMap;

public class SourceSpecificationBuilder implements SourceTypeSetter, SourceAfterTypeSetter {
    private final SourceSpecification sourceSpecification = new SourceSpecification();

    //private static final String API_MAP_STATIC_URL = "http://124.205.31.82:32081/api/map";
    //private static final String API_MAP_STATIC_URL = "http://192.168.7.8:32081/api/map";
    // 改为相对路径
    private static final String API_MAP_STATIC_URL = "api/map";

    private SourceSpecificationBuilder() {}

    public static SourceTypeSetter builder() {
        return new SourceSpecificationBuilder();
    }

    @Override
    public SourceAfterTypeSetter type(String type) {
        sourceSpecification.setType(type);
        return this;
    }

    /**
     * GeoJSON 数据源配置类，对应 Mapbox GL Style Spec 中的 geojson source。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#geojson">Mapbox GeoJSON Source</a>
     */
    @Override
    public SourceAfterTypeSetter geojsonSource() {
        return this.type("geojson");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 image source。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#image">Mapbox Image Source</a>
     */
    @Override
    public SourceAfterTypeSetter imageSource() {
        return this.type("image");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 model source。
     * 支持 "model" 和 "batched-model" 类型。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#model">Mapbox Model Source</a>
     */
    @Override
    public SourceAfterTypeSetter modelSource() {
        return this.type("model");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 model source。
     * 支持 "model" 和 "batched-model" 类型。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#model">Mapbox Model Source</a>
     */
    @Override
    public SourceAfterTypeSetter batchedModelSource() {
        return this.type("batched-model");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 raster-array source。
     * 主要用于加载多波段栅格数组数据（如 NetCDF、HDF5 转换后格式）。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#raster-array">Mapbox Raster Array Source</a>
     */
    @Override
    public SourceAfterTypeSetter rasterArraySource() {
        return this.type("raster-array");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 raster-dem source。
     * 主要用于加载地形高程数据（如 Terrarium 或 Mapbox 编码格式）。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#raster-dem">Mapbox Raster DEM Source</a>
     */
    @Override
    public SourceAfterTypeSetter rasterDEMSource() {
        return this.type("raster-dem");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 raster source。
     * 主要用于加载瓦片图像数据（如卫星影像、地图切片等）。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#raster">Mapbox Raster Source</a>
     */
    @Override
    public SourceAfterTypeSetter rasterSource() {
        return this.type("raster");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 vector source。
     * 主要用于加载矢量瓦片数据（如 PBF 格式）。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#vector">Mapbox Vector Source</a>
     */
    @Override
    public SourceAfterTypeSetter vectorSource() {
        return this.type("vector");
    }

    /**
     * 表示 Mapbox GL 样式规范中的 video source。
     * 主要用于加载一个或多个视频资源，并将其叠加在地图上的指定区域。
     * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/sources/#video">Mapbox Video Source</a>
     */
    @Override
    public SourceAfterTypeSetter videoSource() {
        return this.type("video");
    }

    @Override
    public SourceAfterTypeSetter unknownSource() {
        return this.type("unknown");
    }

    @Override
    public SourceSpecification build() {
        return sourceSpecification;
    }




    public static HashMap<String, SourceSpecification> generateTiandituSources(String systemId) {
        HashMap<String, SourceSpecification> sources = new HashMap<>();

        addTiandituRasterSource(sources, "tdtImgSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/img_c/4490/{z}/{x}/{y}.webp");
        addTiandituRasterSource(sources, "tdtTerSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/ter_c/4490/{z}/{x}/{y}.webp");
        addTiandituRasterSource(sources, "tdtCvaSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/cva_c/4490/{z}/{x}/{y}.webp");
        addTiandituRasterSource(sources, "tdtCiaSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/cia_c/4490/{z}/{x}/{y}.webp");
        addTiandituRasterSource(sources, "tdtCtaSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/cta_c/4490/{z}/{x}/{y}.webp");
        addTiandituRasterSource(sources, "tdtVecSource",
                API_MAP_STATIC_URL + "/sources/raster/public/tile/tianditu/" + systemId + "/vec_c/4490/{z}/{x}/{y}.webp");

        return sources;
    }

    private static void addTiandituRasterSource(HashMap<String, SourceSpecification> sources, String key, String url) {
        SourceSpecification rasterSource = SourceSpecificationBuilder.builder().rasterSource().build();
        rasterSource.setType("raster");
        rasterSource.setTiles(Collections.singletonList(url));
        rasterSource.setTileSize(256);
        rasterSource.setScheme("xyz");
        sources.put(key, rasterSource);
    }

    public static HashMap<String, SourceSpecification> generateGlobalTerrainSources(String sourceKey) {
        HashMap<String, SourceSpecification> sources = new HashMap<>();
        SourceSpecification globalTerrainSource = new SourceSpecificationBuilder().rasterDEMSource().build();
        String url = API_MAP_STATIC_URL + "/terrain/source/wmts/private/business/forestry%3Aglobal_terrain/{z}/{x}/{y}.png";
        globalTerrainSource.setTiles(Collections.singletonList(url));
        globalTerrainSource.setTileSize(256);
        sources.put(sourceKey, globalTerrainSource);
        return sources;
    }
}
