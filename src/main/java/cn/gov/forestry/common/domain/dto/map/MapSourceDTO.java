package cn.gov.forestry.common.domain.dto.map;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MapSourceDTO {
    /**
     * 唯一id,与mapbox数据源无关,增删改查主键用
     * */
    private String id;

    /**
     * 系统id,隔离各个系统
     * */
    private String systemId;

    /**
     * styleId,隔离同一个系统之间各个style
     * */
    private String styleName;

    /**
     * 如果按照每个表分别出了数据源,这里留一个字段存tableId
     * */
    private String tableId;

    /**
     * mapbox的数据源key增删改查不用,为了关联图层控制,各个layer的source应该取这个值
     * */
    private String sourceKey;

    /**
     * 获取数据源类型（如 "geojson", "raster" 等）
     */
    private String type;

    // ----- 各类型数据不同的属性

    /**
     * type: vector, raster, image, raster-array, raster-dem
     * 数据资源的 URL 地址。
     */
    private String url;

    /**
     * type: video
     * 视频资源地址列表（支持多个格式以兼容不同浏览器）。
     * 例如：["video.mp4", "video.webm"]
     */
    private List<String> urls;

    /**
     * type: geojson
     * GeoJSON 数据内容或数据地址 URL。
     * 可以是 GeoJSON 对象（FeatureCollection）或者字符串形式的 URL。
     */
    private Object data;

    /**
     * type: vector, raster, model, raster-array, raster-dem
     * 瓦片地址模板列表，例如：["tiles/{z}/{x}/{y}.pbf"]
     */
    private List<String> tiles;

    /**
     * type: vector, raster, raster-array, raster-dem
     * 数据覆盖范围，格式为 [west, south, east, north]。
     */
    private List<Double> bounds;

    /**
     * type: vector, raster
     * 瓦片编码方案，支持："xyz" 或 "tms"。
     */
    private String scheme; // "xyz" or "tms"

    /**
     * type: vector, raster, geojson, model. raster-array, raster-dem
     * 最小缩放级别，仅在该级别及以上显示数据。
     */
    private Integer minZoom;

    /**
     * type: vector, raster, geojson, model, raster-array, raster-dem
     * 最大缩放级别，仅在该级别及以下显示数据。
     */
    private Integer maxZoom;

    /**
     * type: raster, raster-array, raster-dem
     * 瓦片大小（像素），默认为 512。
     */
    private Integer tileSize;
    /**
     * type: vector, raster, geojson, raster-array, raster-dem
     * 版权信息，通常显示在地图左下角。
     */
    private String attribution;

    /**
     * type: vector, geojson
     * 指定图层 ID 映射关系，例如：{"layer1": "id", "layer2": "gid"}。
     */
    private Map<String, String> promoteId;

    /**
     * type: vector, raster, raster-array, raster-dem
     * 是否标记为易失性数据源（是否频繁变化）。
     */
    private Boolean isVolatile;


    /**
     * type: geojson
     * 多边形缓冲区大小（以瓦片尺寸为单位）。
     */
    private Integer buffer;

    /**
     * type: geojson
     * 过滤器表达式，用于筛选要素。
     * 例如：["==", "$type", "Polygon"]
     */
    private List<Object> filter;

    /**
     * type: geojson
     * 简化几何图形的容差值（单位：像素）。
     */
    private Double tolerance;

    /**
     * type: geojson
     * 是否启用聚类功能。
     */
    private Boolean cluster;

    /**
     * type: geojson
     * 聚类点半径（单位：像素）。
     */
    private Integer clusterRadius;

    /**
     * type: geojson
     * 最大聚类缩放级别（超过此级别后不再聚类）。
     */
    private Integer clusterMaxZoom;

    /**
     * type: geojson
     * 触发聚类所需的最小点数。
     */
    private Integer clusterMinPoints;

    /**
     * type: geojson
     * 自定义聚类属性计算表达式。
     * 示例：{"point_count": ["+", ["get", "point_count"]]}
     */
    private Map<String, Object> clusterProperties;

    /**
     * type: geojson
     * 是否启用线要素的长度、面积等度量信息。
     */
    private Boolean lineMetrics;

    /**
     * type: geojson
     * 是否为每个要素自动生成唯一 ID。
     */
    private Boolean generateId;


    /**
     * type: geojson
     * 是否允许动态更新数据。
     */
    private Boolean dynamic;

    /**
     * type: image, video
     * 图像边界坐标，格式为四个经纬度点组成的数组：
     * [[west, south], [east, south], [east, north], [west, north]]
     */
    private List<List<Double>> coordinates;

    /**
     * type: raster-array
     * 栅格图层描述信息，可为 List<Map<String, Object>> 或自定义对象。
     */
    private Object rasterLayers;

    /**
     * type: raster-dem
     * 高程数据编码方式，支持："terrarium" 或 "mapbox"。
     */
    private String encoding;

    /**
     * 可选：支持任意扩展字段
     * */
    private Map<String, Object> additionalProperties;
}
