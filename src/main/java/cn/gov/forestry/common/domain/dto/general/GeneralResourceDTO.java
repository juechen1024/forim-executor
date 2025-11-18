package cn.gov.forestry.common.domain.dto.general;

import cn.gov.forestry.common.domain.bo.SystemResourceTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 类名称：SysResourceDTO<br>
 * 类描述：<br>
 * 创建时间：2021年12月06日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
@Data
public class GeneralResourceDTO {
    private String systemId;
    private String id;
    private String parentId;

    private String resourceName;
    private String resourceKey;

    private String resourceCustomAttribute;

    private String resourceAlia;

    /**
     * @see SystemResourceTypeEnum
     * */
    private Integer resourceType;

    private String resource;

    private Boolean isBuildIn;

    private Date createTime;

    private Date updateTime;

    private Long resourceOrder;
    private Map<String, Object> additionalProperties;
}
