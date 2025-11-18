package cn.gov.forestry.common.domain.dto.auth;


import lombok.Data;

@Data
public class AuthMenuDTO {
    private String id;
    private String parentId;
    private String path;
    private String title;
    private String icon;
    private String isLink;
    private Boolean close;
    private Integer resourceType;
}
