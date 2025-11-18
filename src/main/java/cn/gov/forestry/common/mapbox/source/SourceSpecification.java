package cn.gov.forestry.common.mapbox.source;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceSpecification {

    /**
     * 获取数据源类型（如 "geojson", "raster" 等）
     * 可以是 "model" 或 "batched-model"。
     */
    @JsonProperty("type")
    private String type;

    // ----- 各类型数据不同的属性

    /**
     * type: vector, raster, image, raster-array, raster-dem
     * 数据资源的 URL 地址。
     */
    @JsonProperty("url")
    private String url;

    /**
     * type: video
     * 视频资源地址列表（支持多个格式以兼容不同浏览器）。
     * 例如：["video.mp4", "video.webm"]
     */
    @JsonProperty("urls")
    private List<String> urls;

    /**
     * type: geojson
     * GeoJSON 数据内容或数据地址 URL。
     * 可以是 GeoJSON 对象（FeatureCollection）或者字符串形式的 URL。
     */
    @JsonProperty("data")
    private Object data;

    /**
     * type: vector, raster, model, raster-array, raster-dem
     * 瓦片地址模板列表，例如：["tiles/{z}/{x}/{y}.pbf"]
     */
    @JsonProperty("tiles")
    private List<String> tiles;

    /**
     * type: vector, raster, raster-array, raster-dem
     * 数据覆盖范围，格式为 [west, south, east, north]。
     */
    @JsonProperty("bounds")
    private List<Double> bounds;

    /**
     * type: vector, raster
     * 瓦片编码方案，支持："xyz" 或 "tms"。
     */
    @JsonProperty("scheme")
    private String scheme; // "xyz" or "tms"

    /**
     * type: vector, raster, geojson, model. raster-array, raster-dem
     * 最小缩放级别，仅在该级别及以上显示数据。
     */
    @JsonProperty("minzoom")
    private Integer minZoom;

    /**
     * type: vector, raster, geojson, model, raster-array, raster-dem
     * 最大缩放级别，仅在该级别及以下显示数据。
     */
    @JsonProperty("maxzoom")
    private Integer maxZoom;

    /**
     * type: raster, raster-array, raster-dem
     * 瓦片大小（像素），默认为 512。
     */
    @JsonProperty("tileSize")
    private Integer tileSize;

    /**
     * type: vector, raster, geojson, raster-array, raster-dem
     * 版权信息，通常显示在地图左下角。
     */
    @JsonProperty("attribution")
    private String attribution;

    /**
     * type: vector, geojson
     * 指定图层 ID 映射关系，例如：{"layer1": "id", "layer2": "gid"}。
     */
    @JsonProperty("promoteId")
    private Map<String, String> promoteId;

    /**
     * type: vector, raster, raster-array, raster-dem
     * 是否标记为易失性数据源（是否频繁变化）。
     */
    @JsonProperty("volatile")
    private Boolean isVolatile;

    /**
     * type: geojson
     * 多边形缓冲区大小（以瓦片尺寸为单位）。
     */
    @JsonProperty("buffer")
    private Integer buffer;

    /**
     * type: geojson
     * 过滤器表达式，用于筛选要素。
     * 例如：["==", "$type", "Polygon"]
     */
    @JsonProperty("filter")
    private List<Object> filter;

    /**
     * type: geojson
     * 简化几何图形的容差值（单位：像素）。
     */
    @JsonProperty("tolerance")
    private Double tolerance;

    /**
     * type: geojson
     * 是否启用聚类功能。
     */
    @JsonProperty("cluster")
    private Boolean cluster;

    /**
     * type: geojson
     * 聚类点半径（单位：像素）。
     */
    @JsonProperty("clusterRadius")
    private Integer clusterRadius;

    /**
     * type: geojson
     * 最大聚类缩放级别（超过此级别后不再聚类）。
     */
    @JsonProperty("clusterMaxZoom")
    private Integer clusterMaxZoom;

    /**
     * type: geojson
     * 触发聚类所需的最小点数。
     */
    @JsonProperty("clusterMinPoints")
    private Integer clusterMinPoints;

    /**
     * type: geojson
     * 自定义聚类属性计算表达式。
     * 示例：{"point_count": ["+", ["get", "point_count"]]}
     */
    @JsonProperty("clusterProperties")
    private Map<String, Object> clusterProperties;

    /**
     * type: geojson
     * 是否启用线要素的长度、面积等度量信息。
     */
    @JsonProperty("lineMetrics")
    private Boolean lineMetrics;

    /**
     * type: geojson
     * 是否为每个要素自动生成唯一 ID。
     */
    @JsonProperty("generateId")
    private Boolean generateId;

    /**
     * type: geojson
     * 是否允许动态更新数据。
     */
    @JsonProperty("dynamic")
    private Boolean dynamic;

    /**
     * type: image, video
     * 图像边界坐标，格式为四个经纬度点组成的数组：
     * [[west, south], [east, south], [east, north], [west, north]]
     */
    @JsonProperty("coordinates")
    private List<List<Double>> coordinates;

    /**
     * type: raster-array
     * 栅格图层描述信息，可为 List<Map<String, Object>> 或自定义对象。
     */
    @JsonProperty("raster-layers")
    private Object rasterLayers;

    /**
     * type: raster-dem
     * 高程数据编码方式，支持："terrarium" 或 "mapbox"。
     */
    @JsonProperty("encoding")
    private String encoding; // "terrarium" or "mapbox"

    /**
     * 可选：支持任意扩展字段
     * */
    @JsonProperty("additional_properties")
    private Map<String, Object> additionalProperties;
}