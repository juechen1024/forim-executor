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
public enum FontFormatTypeEnum {
    TTC("ttc",".ttc"),
    TTF("ttf",".ttf");

    @Getter
    private String format;
    @Getter
    private String extension;

    FontFormatTypeEnum(String format, String extension){
        this.format = format;
        this.extension = extension;
    }

    public static FontFormatTypeEnum getByFormat(String format){
        for (FontFormatTypeEnum value : FontFormatTypeEnum.values()) {
            if (value.getFormat().equals(format)){
                return value;
            }
        }
        return null;
    }

    public static Boolean isTTF(String formatOrExtension){
        return TTF.getFormat().equals(formatOrExtension) || TTF.getExtension().equals(formatOrExtension);
    }
    public static Boolean isTTC(String formatOrExtension){
        return TTC.getFormat().equals(formatOrExtension) || TTC.getExtension().equals(formatOrExtension);
    }
}
