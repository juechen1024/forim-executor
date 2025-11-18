package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

/**
 * 类名称：UserRealmEnum<br>
 * 类描述：sys表的用户对应业务系统的人员或单位类型<br>
 * 创建时间：2022年05月12日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public enum UserRealmTypeEnum {
    FORBIDDEN(-1,"禁止访问"),
    SUPER_ADMIN(0,"超级管理员"),
    ADMIN(1,"管理员"),
    PERSON(10,"人员"),
    COMPANY(20,"单位")
    ;

    @Getter
    private Integer code;

    @Getter
    private String name;

    UserRealmTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static UserRealmTypeEnum getUserRealmTypeByCode(Integer code){
        for (UserRealmTypeEnum value : UserRealmTypeEnum.values()) {
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return FORBIDDEN;
    }
    public static Boolean isSuperAdmin(Integer code) {
        return SUPER_ADMIN.getCode().equals(code);
    }
    public static Boolean isAdmin(Integer code) {
        return ADMIN.getCode().equals(code);
    }
    public static Boolean isCommonUser(Integer code) {
        return PERSON.getCode().equals(code) || COMPANY.getCode().equals(code);
    }
}
