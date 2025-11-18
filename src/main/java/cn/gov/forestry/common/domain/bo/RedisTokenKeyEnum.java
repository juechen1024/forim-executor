package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

/**
 * 类名称：RedisTokenKeyEnum<br>
 * 类描述：<br>
 * 创建时间：2022年01月10日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public enum RedisTokenKeyEnum {
    CURRENT_PATH_TOKEN("general:user:token:path");

    @Getter
    private String key;

    RedisTokenKeyEnum(String key){
        this.key = key;
    }

    public String getCurrentUserTokenKeyPath(String token){
        return CURRENT_PATH_TOKEN.getKey() + ":" + token;
    }
}
