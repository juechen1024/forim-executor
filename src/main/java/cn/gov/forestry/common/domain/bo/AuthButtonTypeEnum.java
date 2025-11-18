package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum AuthButtonTypeEnum {
    ADD("add", "添加"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除")
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    AuthButtonTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }
    public static Boolean isValidedCode(String code) {
        for (AuthButtonTypeEnum value : AuthButtonTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public static Boolean isAdd(String code) {
        return ADD.getCode().equals(code);
    }
    public static Boolean isEdit(String code) {
        return EDIT.getCode().equals(code);
    }
    public static Boolean isDelete(String code) {
        return DELETE.getCode().equals(code);
    }
}
