package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemBuildInFieldEnum {
    ID("id", 1),
    GEOMETRY("geometry", 2),
    GEOMETRY_BBOX("geometry_bbox", 2),
    GEOMETRY_AREA("geometry_area", 2),
    ORDER("order", 3)
    ;
    @Getter
    private String fieldName;

    private Integer fieldFilter;


    SystemBuildInFieldEnum(String fieldName, Integer fieldFilter) {
        this.fieldName = fieldName;
        this.fieldFilter = fieldFilter;
    }


}
