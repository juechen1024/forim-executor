package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.file.FileContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "assetsInnerResourceClient", url = "${forim.inner.assets.url}")
public interface AssetsInnerResourceClient {
    /**
     * name字段带相对路径,如/sprite/a.png
     * 实际存储路径assets模块会拼接上systemId区分开,所以请求时只需要按照各个业务模块区分就行
     * */
    @PostMapping(value = "/inner/resource/put/file")
    FileContent putResourceFile(@RequestBody FileContent fileContent);

    @PostMapping(value = "/inner/resource/get/file")
    FileContent getResourceFile(@RequestBody FileContent fileContent);
}
