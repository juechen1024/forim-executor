package cn.gov.forestry.common.domain.dto.map;

import cn.gov.forestry.common.attachment.AttachmentUploadFile;
import lombok.Data;

import java.util.List;

@Data
public class MapGlyphsDTO {
    private String id;
    private String systemId;
    private String styleName;
    private List<AttachmentUploadFile> mapGlyphFiles;
}
