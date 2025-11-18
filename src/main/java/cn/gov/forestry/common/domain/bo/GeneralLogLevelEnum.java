package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum GeneralLogLevelEnum {
    DEBUG("debug", 100),
    INFO("info", 200),
    ERROR("error", 500)
    ;

    @Getter
    private String code;
    @Getter
    private Integer level;

    GeneralLogLevelEnum(String code, Integer level) {
        this.code = code;
        this.level = level;
    }
}
