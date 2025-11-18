package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum RedisTokenUserInfoKeyEnum {
    CURRENT_USER_INFO_TOKEN("general:user:token:info");

    @Getter
    private String key;

    RedisTokenUserInfoKeyEnum(String key){
        this.key = key;
    }

    public String getCurrentUserInfoTokenKey(String token){
        return CURRENT_USER_INFO_TOKEN.getKey() + ":" + token;
    }
}
