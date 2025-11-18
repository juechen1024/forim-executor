package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemOperatorTypeEnum {
    QUERY("10","查询"),
    CREATE("20","新增"),
    UPDATE("30","更新"),
    DELETE("40","删除")
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    SystemOperatorTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
}
