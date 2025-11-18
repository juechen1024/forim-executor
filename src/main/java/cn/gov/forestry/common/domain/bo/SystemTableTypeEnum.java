package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemTableTypeEnum {
    NORMAL_TABLE("10", "普通表"),
    GEOMETRY_TABLE("20", "空间表"),
    RASTER_TABLE("30", "栅格表"),
    ;
    @Getter
    private String code;
    @Getter
    private String label;

    SystemTableTypeEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static Boolean isGeometryTable(String code) {
        return GEOMETRY_TABLE.getCode().equals(code);
    }
    public static Boolean isRasterTable(String code) {
        return RASTER_TABLE.getCode().equals(code);
    }
}
