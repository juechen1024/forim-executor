package cn.gov.forestry.common.domain.dto.map;

import lombok.Data;

@Data
public class MapGlyphsGenerateDTO {
    private String systemId;
    /**
     * 就是assets返回的glyphs的resourcePath
     * 不可与outputRelPath相同
     * */
    private String inputRelPath;
    /**
     * 最好是inputpath的同级别目录
     * 不可与inputRelPath相同
     * */
    private String outputRelPath;
    /**
     * @see cn.gov.forestry.common.domain.bo.FontFormatTypeEnum
     * */
    private String extension;
}
