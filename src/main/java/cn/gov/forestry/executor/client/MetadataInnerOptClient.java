package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.domain.dto.metadata.MetadataFieldDTO;
import cn.gov.forestry.common.domain.dto.metadata.MetadataTableDTO;
import cn.gov.forestry.common.domain.dto.metadata.batch.MetadataFieldBatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "metadataInnerOptClient", url = "${forim.inner.metadata.url}")
public interface MetadataInnerOptClient {
    @PostMapping(value = "/inner/operator/metadata/sys/update/table/additional/properties")
    MetadataTableDTO updateMetadataTableAdditionalProperties(@RequestBody MetadataTableDTO metadataTableDTO);

    @PostMapping(value = "/inner/operator/metadata/sys/create/field")
    MetadataFieldDTO createField(@RequestBody MetadataFieldDTO dto);

    @PostMapping(value = "/inner/operator/metadata/sys/create/field/batch")
    MetadataFieldBatchDTO createFieldBatch(@RequestBody MetadataFieldBatchDTO dto);
}
