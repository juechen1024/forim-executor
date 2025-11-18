package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemFieldDataTypeEnum {

    INTEGER("integer", "整型", "Integer", "number"),
    DOUBLE("double", "浮点型", "Double", "number"),
    BOOLEAN("boolean", "布尔值", "Boolean", "boolean"),
    STRING("string", "字符串", "String", "string"),
    DATE("date", "时间类型", "Date", "string"),
    OBJECT("object", "对象", "Document", "object"),
    ARRAY("array", "对象数组", "Array", "object[]");

    @Getter
    private final String code;
    @Getter
    private final String label;
    @Getter
    private final String dbDataType;
    @Getter
    private final String tsDataType;// 新增：Java 对应的 Class 类型

    SystemFieldDataTypeEnum(String code, String label, String dbDataType, String tsDataType) {
        this.code = code;
        this.label = label;
        this.dbDataType = dbDataType;
        this.tsDataType = tsDataType;
    }

    /**
     * 根据 code 获取对应的枚举实例，未找到时返回 STRING 作为默认值
     *
     * @param code 枚举编码
     * @return 对应的枚举实例，未找到时返回 STRING
     */
    public static SystemFieldDataTypeEnum getByCodeOrDefault(String code) {
        if (code == null) {
            return STRING;
        }
        for (SystemFieldDataTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return STRING; // 未找到时，返回 STRING 作为默认兜底
    }

}