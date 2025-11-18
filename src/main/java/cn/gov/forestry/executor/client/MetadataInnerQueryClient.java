package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.domain.dto.metadata.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "metadataInnerQueryClient", url = "${forim.inner.metadata.url}")
public interface MetadataInnerQueryClient {
    @PostMapping("/inner/query/metadata/sys/table/info")
    MetadataTableDTO getMetadataTableInfo(@RequestBody MetadataTableDTO dto);

    @PostMapping("/inner/query/metadata/sys/table/permission")
    MetadataTablePermissionDTO getMetadataTablePermission(@RequestBody MetadataTablePermissionDTO dto);

    @PostMapping(value = "/inner/query/metadata/sys/table/list")
    List<MetadataTableDTO> getMetadataTableList(@RequestBody MetadataTableDTO dto);

    @PostMapping("/inner/query/metadata/sys/field/info")
    MetadataFieldDTO getMetadataFieldInfo(@RequestBody MetadataFieldDTO dto);
    @PostMapping("/inner/query/metadata/sys/field/list/by/table")
    List<MetadataFieldDTO> getMetadataFieldListByTable(@RequestBody MetadataFieldDTO dto);

    @PostMapping("/inner/query/metadata/sys/enum/info")
    MetadataEnumDTO getMetadataEnumInfo(@RequestBody MetadataEnumDTO dto);

    @PostMapping("/inner/query/metadata/sys/code/list/by/enum")
    List<MetadataCodeDTO> getMetadataCodeListByEnum(@RequestBody MetadataCodeDTO dto);
}
