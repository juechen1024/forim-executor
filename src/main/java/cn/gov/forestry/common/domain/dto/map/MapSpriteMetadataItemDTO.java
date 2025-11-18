package cn.gov.forestry.common.domain.dto.map;

import lombok.Data;

@Data
public class MapSpriteMetadataItemDTO {
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private Integer pixelRatio;

    public MapSpriteMetadataItemDTO() {}

    public MapSpriteMetadataItemDTO(Integer width, Integer height, Integer x, Integer y) {
        this(width, height, x, y, null);
    }

    public MapSpriteMetadataItemDTO(Integer width, Integer height, Integer x, Integer y, Integer pixelRatio) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.pixelRatio = pixelRatio;
    }
}
