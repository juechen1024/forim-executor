package cn.gov.forestry.common.domain.po;

import lombok.Data;

import java.util.Date;

@Data
public class BaseInfo {
    private Date createTime;

    private Date updateTime;

    private Long createUserId;

    private String createUserName;

    private Integer createUserTypeCode;

    private String createUserTypeName;

    private Boolean enabled;
}
