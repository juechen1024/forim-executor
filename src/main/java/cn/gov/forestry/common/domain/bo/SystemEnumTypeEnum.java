package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemEnumTypeEnum {
    NORMAL_ENUM("10", "普通枚举"),
    TABLE_ENUM("20", "表枚举")
    ;
    @Getter
    private String code;
    @Getter
    private String label;

    SystemEnumTypeEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

}
