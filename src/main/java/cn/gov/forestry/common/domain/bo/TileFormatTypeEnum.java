package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

/**
 * 类名称：TileFormatTypeEnum<br>
 * 类描述：<br>
 * 创建时间：2022年02月20日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public enum TileFormatTypeEnum {
    PNG("png","image/png"),
    JPG("jpg","image/jpeg"),
    JPEG("jpeg","image/jpeg"),
    WEBP("webp","image/webp"),
    PBF("pbf","application/vnd.mapbox-vector-tile");

    @Getter
    private String format;
    @Getter
    private String contentType;

    TileFormatTypeEnum(String format, String contentType){
        this.format = format;
        this.contentType = contentType;
    }
}
