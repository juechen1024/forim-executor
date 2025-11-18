package cn.gov.forestry.common.service;

import cn.gov.forestry.common.domain.bo.RedisSeriesKeyEnum;

public interface SeriesKeyService {
    Long generateSeriesKey(RedisSeriesKeyEnum redisSeriesKeyEnum);
    Long generateSeriesKey(RedisSeriesKeyEnum redisSeriesKeyEnum, Integer incrementDelta);
    Long generatePruductsTableOrder(String systemId, String tableId, String orderField);
}
