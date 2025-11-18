package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.database.crud.InsertBatchParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "databaseInnerCRUDClient", url = "${forim.inner.database.url}")
public interface DatabaseInnerCRUDClient {
    /**
     * 批量插入多条记录到指定数据表
     * @apiNote param.properties中必须有id!
     *
     * @param params 包含数据库连接信息、表名、多条记录字段值列表的插入参数对象
     * @return 返回每个成功插入记录的主键 ID 列表，顺序与输入一致；若部分失败，对应位置可能为 null
     * @throws RuntimeException 当数据库操作失败、参数无效或连接异常时抛出
     */
    @PostMapping("/inner/crud/insert/batch")
    List<String> insertBatch(@RequestBody InsertBatchParams params);
}
