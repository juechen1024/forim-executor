package cn.gov.forestry.common.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * 类名称：LoginResultVO<br>
 * 类描述：<br>
 * 创建时间：2021年12月07日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserRealmInfoDTO {
    private final static String INNER_TOKEN = "inner";

    private String systemId;

    private String token;

    private String userName;

    private String userAlia;

    private String userRealName;

    private String userPhone;

    private String userEmail;

    private Integer userRealmTypeCode;

    private String userRealmTypeName;

    private String userRealmId;

    private String userAvatarUrl;

    private Map<String, Object> additionalProperties;

    public static AuthUserRealmInfoDTO generateInnerUserRealmInfo() {
        AuthUserRealmInfoDTO result = new AuthUserRealmInfoDTO();
        result.setToken(INNER_TOKEN);
        result.setUserName(INNER_TOKEN);
        return result;
    }

    public Boolean isInnerUser() {
        return token.equals(INNER_TOKEN);
    }

}
