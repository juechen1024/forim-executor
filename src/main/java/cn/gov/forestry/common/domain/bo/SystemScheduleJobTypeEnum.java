package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemScheduleJobTypeEnum {
    IMPORT_EXCEL("201","导入excel"),
    IMPORT_SHAPEFILE("202","导入shapefile"),
    IMPORT_GEOTIFF("203","导入geoTiff"),
    TRANSFER_GLYPHS("301","转换glyphs"),
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    SystemScheduleJobTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static Boolean isImportExcel(String code){
        return IMPORT_EXCEL.getCode().equals(code);
    }

    public static Boolean isImportShapefile(String code){
        return IMPORT_SHAPEFILE.getCode().equals(code);
    }

    public static Boolean isImportGeoTiff(String code){
        return IMPORT_GEOTIFF.getCode().equals(code);
    }

    public static SystemScheduleJobTypeEnum getByCode(String code){
        for(SystemScheduleJobTypeEnum jobTypeEnum : SystemScheduleJobTypeEnum.values()){
            if(jobTypeEnum.getCode().equals(code)){
                return jobTypeEnum;
            }
        }
        return null;
    }
}
