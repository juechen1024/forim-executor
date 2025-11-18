package cn.gov.forestry.common.domain.dto.metadata;

import cn.gov.forestry.common.rule.DataFilterRule;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MetadataTablePermissionDTO {
    private String id;

    private String systemId;

    private String systemModule;

    private String roleId;

    private String tableId;

    // CRUD权限
    private Boolean canQuery;

    private Boolean canCreate;

    private Boolean canModify;

    private Boolean canDelete;

    // 高级权限
    private Boolean canAlter;

    private Boolean canDrop;

    private Boolean canGrant;

    // 数据过滤规则集合
    private List<DataFilterRule> dataFilterRules;

    private Map<String, Object> additionalProperties;
}
