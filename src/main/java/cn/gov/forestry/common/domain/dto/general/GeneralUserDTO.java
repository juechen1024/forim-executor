package cn.gov.forestry.common.domain.dto.general;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class GeneralUserDTO {
    private String systemId;
    private String id;
    private String userName;
    private String userAlia;
    private String userPassword;
    private String userPhone;
    private String userEmail;
    private String userRealName;
    private String userLocal;
    private Date userLastLoginTime;
    private String userCurrentToken;

    private Integer userRealmTypeCode;

    private String userRealmTypeName;

    private String userAvatarUrl;

    private Boolean loginEnabled;

    private Boolean applyReset;

    // po -> dto empty
    // service里查询
    private List<GeneralRoleDTO> userRoleList;

    // po <-> dto
    private List<String> userRole;

    private Boolean isBuildIn;
    private Date createTime;
    private Date updateTime;
    private Long userOrder;
    private Map<String, Object> additionalProperties;
}
