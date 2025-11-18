package cn.gov.forestry.common.service.impl;

import cn.gov.forestry.common.domain.bo.RedisSeriesKeyEnum;
import cn.gov.forestry.common.service.SeriesKeyService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeriesKeyServiceImpl implements SeriesKeyService {
    private final static Integer INCREMENT_DELTA = 1;
    private final RedisTemplate<String, String> redisTemplate;

    public SeriesKeyServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long generateSeriesKey(RedisSeriesKeyEnum redisSeriesKeyEnum) {
        return redisTemplate.opsForValue().increment(redisSeriesKeyEnum.getKey(), INCREMENT_DELTA);
    }

    @Override
    public Long generateSeriesKey(RedisSeriesKeyEnum redisSeriesKeyEnum, Integer incrementDelta) {
        return redisTemplate.opsForValue().increment(redisSeriesKeyEnum.getKey(), incrementDelta);
    }

    @Override
    public Long generatePruductsTableOrder(String systemId, String tableId, String orderField) {
        String key = RedisSeriesKeyEnum.getProductsTableRecordOrderKey(systemId, tableId, orderField);
        return redisTemplate.opsForValue().increment(key, INCREMENT_DELTA);
    }
}
