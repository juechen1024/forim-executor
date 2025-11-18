package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.file.FileContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "exchangeInnerTransferClient", url = "${forim.inner.exchange.url}")
public interface ExchangeInnerTransferClient {
    @PostMapping("/inner/transfer/content/excel/to/list")
    List<Map<String, Object>> transferExcelToList(@RequestBody FileContent fileContent);

    @PostMapping("/inner/transfer/content/shapefile/zip/to/list")
    List<Map<String, Object>> transferShapefileZipToList(@RequestBody FileContent fileContent);

    @PostMapping("/inner/transfer/content/geo/tiff/to/list")
    List<Map<String, Object>> transferGeoTiffToList(@RequestBody FileContent fileContent);

    /**
     * 导出模板
     * id: "", (sheetName tableId)
     * fileName: "",
     * fieldList: [
     *  {
     *      fieldName: "",
     *      fieldTitleName: ""
     *  }
     * ]
     *
     * */
    @PostMapping("/inner/transfer/content/list/to/excel/template")
    FileContent transferListToExcelTemplate(@RequestBody Map<String, Object> data);

    /**
     * 导出excel数据
     * id: "", (sheetName tableId)
     * fileName: "",
     * fieldList: [
     *  {
     *      fieldName: "",
     *      fieldTitleName: ""
     *  }
     * ]
     * dataList: [
     *  {
     *
     *  }
     * ]
     * */
    @PostMapping("/inner/transfer/content/list/to/excel/content")
    FileContent transferListToExcelContent(@RequestBody Map<String, Object> data);
}
