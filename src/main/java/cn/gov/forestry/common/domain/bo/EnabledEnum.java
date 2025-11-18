package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum EnabledEnum {
    ENABLE(0, "启用", Boolean.TRUE),
    DISABLE(1, "禁用", Boolean.FALSE)
    ;
    @Getter
    private Integer statusCode;
    @Getter
    private String statusDesc;
    @Getter
    private Boolean status;

    EnabledEnum(Integer statusCode, String statusDesc, Boolean status) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.status = status;
    }

    public static Boolean isEnabled(Integer statusCode) {
        return ENABLE.getStatusCode().equals(statusCode);
    }
}
