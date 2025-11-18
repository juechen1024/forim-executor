package cn.gov.forestry.common.domain.dto.metadata;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MetadataTableDTO {
    private String id;

    private String systemId;

    private String systemModule;

    private String tableName;

    private String tableTitleName;

    private String tableAlia;

    /**
     * @see cn.gov.forestry.metadata.domain.bo.MetadataTableTypeEnum
     * @see cn.gov.forestry.common.domain.bo.SystemTableTypeEnum
     * */
    private String tableType;

    private String tableGroup;

    private String tableTheme;

    private String tableActionType;

    private String tableGeometryType;

    private Boolean isPublishedAsEnum;

    private String asEnumLabelFieldId;

    private String asEnumValueFieldId;

    // 审计日志级别
    private Integer auditLogLevel;

    private String tableEntityName;

    private Long tableFieldCounts;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String createUserName;

    private Map<String, Object> additionalProperties;
}
