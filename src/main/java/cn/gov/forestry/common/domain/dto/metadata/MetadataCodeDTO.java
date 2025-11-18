package cn.gov.forestry.common.domain.dto.metadata;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MetadataCodeDTO {
    private String systemId;

    private String systemModule;

    private String id;

    private String enumId;

    private String codeValue;

    private String codeLabel;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String createUserName;

    private Map<String, Object> additionalProperties;

}
