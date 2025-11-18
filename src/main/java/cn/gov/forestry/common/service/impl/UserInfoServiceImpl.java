package cn.gov.forestry.common.service.impl;

import cn.gov.forestry.common.domain.bo.RedisTokenUserInfoKeyEnum;
import cn.gov.forestry.common.domain.dto.auth.AuthUserRealmInfoDTO;
import cn.gov.forestry.common.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserInfoServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void saveUserRealmInfo(AuthUserRealmInfoDTO authUserRealmInfoDTO) throws JsonProcessingException {
        String currentUserInfoTokenKey = RedisTokenUserInfoKeyEnum.CURRENT_USER_INFO_TOKEN.getCurrentUserInfoTokenKey(authUserRealmInfoDTO.getToken());
        redisTemplate.opsForValue().set(currentUserInfoTokenKey, objectMapper.writeValueAsString(authUserRealmInfoDTO));
        redisTemplate.expire(currentUserInfoTokenKey,30, TimeUnit.DAYS);
    }

    @Override
    public AuthUserRealmInfoDTO getUserRealmInfo(String token) throws JsonProcessingException {
        if (ObjectUtils.isEmpty(token)){
            return new AuthUserRealmInfoDTO();
        }
        String currentUserInfoTokenKey = RedisTokenUserInfoKeyEnum.CURRENT_USER_INFO_TOKEN.getCurrentUserInfoTokenKey(token);
        String userRealmInfoJsonString = redisTemplate.opsForValue().get(currentUserInfoTokenKey);
        return objectMapper.readValue(userRealmInfoJsonString, AuthUserRealmInfoDTO.class);
    }
}
