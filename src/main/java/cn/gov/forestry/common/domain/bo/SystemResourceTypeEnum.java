package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

/**
 * 类名称：SysResourceTypeEnum<br>
 * 类描述：<br>
 * 创建时间：2022年01月11日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public enum SystemResourceTypeEnum {
    URL_API(1,"接口地址"),
    URL_PREFIX(2,"接口前缀"),
    MENU(6,"菜单"),
    BUTTON(8,"按钮"),
    PROP(9,"属性"),
    ROUTER(20,"路由地址"),
    NECROMANTIC_ENTITY(100, "NecromanticEntity")
    ;

    @Getter
    private Integer code;

    @Getter
    private String name;

    SystemResourceTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }
    public static Boolean isButton(Integer code) {
        return BUTTON.getCode().equals(code);
    }
    public static Boolean isMenu(Integer code) {
        return MENU.getCode().equals(code);
    }

    public static Boolean migrationMenuItem(Integer code) {
        return NECROMANTIC_ENTITY.getCode().equals(code) || MENU.getCode().equals(code);
    }
    public static Boolean isProp(Integer code) {
        return PROP.getCode().equals(code);
    }
}
