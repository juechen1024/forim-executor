package cn.gov.forestry.common.domain.dto.geometry;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeometryTileDTO {
    /**
     * 基本参数
     * */
    private String systemId;
    private String tableId;
    private Integer z;
    private Integer x;
    private Integer y;


    /**
     * 额外参数
     * */
    private Integer tileSize;
    private Integer extent;

    // 数据查询参数
    private Map<String, Object> queryParams;

    // 级别对应的面积过滤条件,如果为空就不过滤
    private List<Double> zoomGeometryAreaFilter;

    // 是否应用面积过滤，区划或者某些图层是需要一直显示的，点图层和线图层的数据库面积为0，也不过滤
    private Boolean useGeometryAreaFilter;

    // 是否使用geometry缓存
    private Boolean useGeometryCache;

    // 是否使用空间索引
    private Boolean useSpatialIndex;

    // 是否抽稀 删除这个参数,如果不需要抽稀就把simplifyStopZoom设置为0就好
    // private Boolean simplify = true;

    // 是否使用内建抽稀策略，如果false就使用simplifyTolerance，否则走内部算法进行抽稀
    private Boolean useBuildInSimplifyToleranceCalculate;

    // 瓦片内元素数量超过阈值就进行抽稀
    private Integer simplifyGeometryThreshold;

    // 瓦片内元素数量超过这个数值就显著增加抽稀容差
    private Integer simplifyGeometryBoundingThreshold;

    // 抽稀容差计算方法 0线形增长 1平方增长 2指数增长 3Sigmoid增长
    private Integer simplifyToleranceCalculateType;

    // 从这个级别停止简化开始返回原始数据
    private Integer simplifyStopZoom;

    // 抽稀容差。useBuildInSimplifyToleranceCalculate为false时生效，如果请求瓦片时传了这个值就优先用这个值，说明在外部计算过
    private Double simplifyTolerance;

    /**
     * 返回结果
    * {
    *   intFeatureMapList: List<Map<String, Object>>
    *   mvtFeatureMapList: List<Map<String, Object>>
    * }
    * */
    private Map<String, Object> tileFeatureMap;

    /**
     * 返回瓦片xyz对应的原始bbox
     * tileOriginalBbox [double,double,double,double] -
     * minx,miny,maxx,maxy -
     * minLng,minLat,maxLng,maxLat
     * */
    private List<Double> tileOriginalBbox;
}
