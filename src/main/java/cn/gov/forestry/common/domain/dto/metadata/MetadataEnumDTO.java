package cn.gov.forestry.common.domain.dto.metadata;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MetadataEnumDTO {

    private String id;

    private String systemId;

    private String systemModule;

    private String enumName;

    private String enumTitleName;

    private String enumAlia;

    /**
     * @see cn.gov.forestry.metadata.domain.bo.MetadataEnumTypeEnum
     * @see cn.gov.forestry.common.domain.bo.SystemEnumTypeEnum
     * */
    private String enumType;

    private String enumGroup;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String createUserName;

    private Map<String, Object> additionalProperties;

}
