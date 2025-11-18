package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.domain.dto.general.GeneralSystemDTO;
import cn.gov.forestry.common.domain.dto.general.GeneralUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "generalInnerQueryClient", url = "${forim.inner.general.url}")
public interface GeneralInnerQueryClient {
    @PostMapping("/inner/query/general/system/info")
    GeneralSystemDTO getSystemInfo(@RequestBody GeneralSystemDTO dto);

    @PostMapping("/inner/query/general/user/info/by/token")
    GeneralUserDTO queryUserInfoByToken(@RequestBody GeneralUserDTO dto);
}
