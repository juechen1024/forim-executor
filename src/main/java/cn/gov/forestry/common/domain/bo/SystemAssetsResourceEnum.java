package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemAssetsResourceEnum {
    GLYPHS("glyphs", "/glyphs/"),
    SPRITE("sprite", "/sprite/"),
    SHAPEFILE("shapefile", "/shapefile/"),
    GEOTIFF("geotiff", "/geotiff/"),
    EXCEL("excel", "/excel/"),
    ;
    @Getter
    private final String resource;

    @Getter
    private final String resourceBasePath;

    SystemAssetsResourceEnum(String resource, String resourceBasePath) {
        this.resource = resource;
        this.resourceBasePath = resourceBasePath;
    }
}
