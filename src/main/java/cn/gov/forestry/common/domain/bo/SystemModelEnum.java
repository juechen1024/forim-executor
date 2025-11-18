package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemModelEnum {
    FORIM_GENERAL("10","基础模块"),
    FORIM_ASSETS("20","文件模块"),
    FORIM_METADATA("30","元数据模块"),
    FORIM_PRODUCTS("50","生产模块"),
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    SystemModelEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
}
