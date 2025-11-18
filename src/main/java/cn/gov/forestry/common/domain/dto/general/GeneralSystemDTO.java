package cn.gov.forestry.common.domain.dto.general;

import cn.gov.forestry.common.attachment.AttachmentUploadFile;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class GeneralSystemDTO {
    private String id;
    private String systemName;
    private String systemLiteName;
    private String systemDescription;
    private String systemType;
    private String systemUrl;
    private String systemAssetsBucket;
    private String systemContent;
    private List<AttachmentUploadFile> systemAvatarFiles;
    private List<AttachmentUploadFile> systemIconFiles;
    private List<AttachmentUploadFile> systemCoverImageFiles;
    private String systemVersion;
    private String systemDatabaseType;
    private String systemDatabaseHost;
    private String systemDatabasePort;
    private String systemDatabaseUsername;
    private String systemDatabasePassword;
    private String systemDatabaseName;
    private Date createTime;
    private Date updateTime;
    private Map<String, Object> additionalProperties;
}
