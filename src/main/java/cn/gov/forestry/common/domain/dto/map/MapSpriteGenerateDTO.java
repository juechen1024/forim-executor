package cn.gov.forestry.common.domain.dto.map;

import cn.gov.forestry.common.file.FileContent;
import lombok.Data;

import java.util.List;

@Data
public class MapSpriteGenerateDTO {
    private List<FileContent> fileContents;
    private Integer scale;
}
