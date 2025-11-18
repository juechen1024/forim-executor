package cn.gov.forestry.common.domain.dto.map;

import cn.gov.forestry.common.domain.bo.TileFormatTypeEnum;
import lombok.Data;

/**
 * 类名称：MapTileDTO<br>
 * 类描述：<br>
 * 创建时间：2022年02月20日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
@Data
public class MapTileDTO {
    private String eTag;
    private String contentLength;

    private TileFormatTypeEnum tileFormatType;
    private byte[] tileBytes;

    public MapTileDTO(TileFormatTypeEnum tileFormatType){
        this.tileBytes = new byte[0];
        this.tileFormatType = tileFormatType;
    }

    public MapTileDTO(byte[] tileBytes, TileFormatTypeEnum tileFormatType){
        this.tileBytes = tileBytes;
        this.tileFormatType = tileFormatType;
    }

}
