package cn.gov.forestry.common.domain.dto.metadata.batch;

import cn.gov.forestry.common.domain.dto.metadata.MetadataFieldDTO;
import lombok.Data;

import java.util.List;

@Data
public class MetadataFieldBatchDTO {
    private String systemId;

    private String systemModule;

    private String tableId;

    private List<MetadataFieldDTO> fields;

    private Long createdCount = 0L;

}
