package cn.gov.forestry.common.domain.dto.map;

import lombok.Data;

import java.util.Map;

@Data
public class MapLayerDTO {

    /**
     * 唯一id,与mapbox图层无关,增删改查主键用
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
     * mapbox属性: id
     * 与图层控制,图层树相关的id,增删改查不用,为了关联图层控制
     * */
    private String layerId;

    /**
     * mapbox属性: type
     * */
    private String type;

    /**
     * mapbox属性: metadata
     * */
    private Map<String, Object> metadata;

    /**
     * mapbox属性: source
     * */
    private String source;

    /**
     * mapbox属性: source-layer
     * */
    private String sourceLayer;

    /**
     * mapbox属性: slot
     * */
    private String slot;

    /**
     * mapbox属性: minzoom
     * */
    private Integer minZoom;

    /**
     * mapbox属性: maxzoom
     * */
    private Integer maxZoom;

    /**
     * mapbox属性: filter
     * */
    private Map<String, Object> filter;

    /**
     * mapbox属性: layout
     * */
    private Map<String, Object> layout;

    /**
     * mapbox属性: paint
     * */
    private Map<String, Object> paint;

    /**
     * 排序属性,查询时按照这个值从小到大排序
     * */
    private Long layerOrder;

    private Map<String, Object> additionalProperties;
}
