package cn.gov.forestry.common.domain.dto.auth;

import lombok.Data;

@Data
public class AuthButtonDTO {
    private String id;
    private String parentId;
    private String key;
    private String buttonType;
}
