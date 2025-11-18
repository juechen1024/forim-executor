package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemFieldTypeEnum {
    PRIMARY_KEY("PrimaryKey", "主键"),
    NORMAL("Normal", "普通字段"),
    TIMESERIES("Timeseries", "时序字段"),
    GEOMETRY("Geometry", "空间字段")
    ;

    @Getter
    private String code;
    @Getter
    private String label;

    SystemFieldTypeEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
