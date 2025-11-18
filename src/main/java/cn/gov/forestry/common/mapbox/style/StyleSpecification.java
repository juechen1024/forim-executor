package cn.gov.forestry.common.mapbox.style;

import cn.gov.forestry.common.mapbox.layer.LayerSpecification;
import cn.gov.forestry.common.mapbox.source.SourceSpecification;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示整个 Mapbox GL 样式的主类。
 * <p>
 * 包含地图样式的所有配置项，包括图层、数据源、相机设置等。
 *
 * @see <a href="https://docs.mapbox.com/mapbox-gl-js/style-spec/">Mapbox GL Style Specification</a>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StyleSpecification {

    /**
     * 样式版本号。目前推荐为 8。
     */
    @JsonProperty("version")
    private Integer version = 8;

    /**
     * 是否是片段样式（可选）
     */
    @JsonProperty("fragment")
    private Boolean fragment;

    /**
     * 样式名称（可选）
     */
    @JsonProperty("name")
    private String name;

    /**
     * 元数据信息（任意结构）
     */
    @JsonProperty("metadata")
    private Object metadata;

    /**
     * 地图中心点 [lng, lat]
     */
    @JsonProperty("center")
    private List<Double> center;

    /**
     * 初始缩放级别
     */
    @JsonProperty("zoom")
    private Double zoom;

    /**
     * 初始旋转角度（单位：度）
     */
    @JsonProperty("bearing")
    private Double bearing;

    /**
     * 初始倾斜角度（单位：度）
     */
    @JsonProperty("pitch")
    private Double pitch;

    /**
     * 光照配置
     */
    @JsonProperty("light")
    private Map<String, Object> light;

    /**
     * 多光源配置（数组）
     */
    @JsonProperty("lights")
    private List<Map<String, Object>> lights;

    /**
     * 地形配置
     */
    @JsonProperty("terrain")
    private Map<String, Object> terrain;

    /**
     * 雾效果配置
     */
    @JsonProperty("fog")
    private Map<String, Object> fog;

    /**
     * @experimental This property is experimental and subject to change in future versions.
     */
    @JsonProperty("snow")
    private Map<String, Object> snow;

    /**
     * @experimental This property is experimental and subject to change in future versions.
     */
    @JsonProperty("rain")
    private Map<String, Object> rain;

    /**
     * 相机配置
     */
    @JsonProperty("camera")
    private Map<String, Object> camera;

    /**
     * 颜色主题配置
     */
    @JsonProperty("color_theme")
    private Map<String, Object> colorTheme;

    /**
     * 室内地图配置
     */
    @JsonProperty("indoor")
    private Map<String, Object> indoor;

    /**
     * 导入其他样式文件配置
     */
    @JsonProperty("imports")
    private List<Map<String, Object>> imports;

    /**
     * 样式 Schema 配置
     */
    @JsonProperty("schema")
    private Map<String, Object> schema;

    /**
     * 数据源集合（sourceId -> SourceSpecification）
     */
    @JsonProperty("sources")
    private Map<String, SourceSpecification> sources;

    /**
     * 精灵图 URL 或 ID（用于图标和字体）
     */
    @JsonProperty("sprite")
    private String sprite;

    /**
     * 字体图集 URL 模板
     */
    @JsonProperty("glyphs")
    private String glyphs;

    /**
     * 过渡动画配置
     */
    @JsonProperty("transition")
    private Map<String, Object> transition;

    /**
     * 投影方式配置
     */
    @JsonProperty("projection")
    private Map<String, Object> projection;

    /**
     * 图层集合（LayerSpecification[]）
     */
    @JsonProperty("layers")
    private List<LayerSpecification> layers;

    /**
     * 3D 模型配置
     */
    @JsonProperty("models")
    private Map<String, Object> models;

    /**
     * 特征集合配置
     */
    @JsonProperty("featuresets")
    private Map<String, Object> featureSets;

    /**
     * 动态属性支持：捕获未知字段
     */
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }
}