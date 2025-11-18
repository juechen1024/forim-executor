package cn.gov.forestry.common.domain.dto.base;

import lombok.Data;

import java.util.Date;

@Data
public class BaseInfoDTO {
    private Date createTime;

    private Date updateTime;

    private Long createUserId;

    private String createUserName;

    private Integer createUserTypeCode;

    private String createUserTypeName;

    private Boolean enabled;
}
