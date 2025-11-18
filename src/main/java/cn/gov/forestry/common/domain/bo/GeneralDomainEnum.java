package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum GeneralDomainEnum {
    GENERAL_SYSTEM("10","用户"),
    GENERAL_USER("20","用户"),
    GENERAL_ROLE("30","角色"),
    GENERAL_RESOURCE("40","资源")
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    GeneralDomainEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
}
