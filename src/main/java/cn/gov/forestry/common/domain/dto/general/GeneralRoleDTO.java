package cn.gov.forestry.common.domain.dto.general;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 类名称：SysRoleDTO<br>
 * 类描述：<br>
 * 创建时间：2021年12月06日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
@Data
public class GeneralRoleDTO {
    private String systemId;

    private String id;

    private String roleName;
    private String roleKey;

    private Integer roleType;

    private String roleAlia;

    private Integer roleLevel;

    // po <-> dto
    private List<String> roleResource;
    // po -> dto empty
    // service里查询
    private List<GeneralResourceDTO> roleResourceList;

    private Boolean isBuildIn;

    private Date createTime;

    private Date updateTime;

    private Long roleOrder;
    private Map<String, Object> additionalProperties;
}
