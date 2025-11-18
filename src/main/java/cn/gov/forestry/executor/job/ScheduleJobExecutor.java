package cn.gov.forestry.executor.job;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.FieldValue;
import cn.gov.forestry.common.database.FieldValueBuilder;
import cn.gov.forestry.common.database.crud.InsertBatchParams;
import cn.gov.forestry.common.domain.bo.*;
import cn.gov.forestry.common.domain.dto.general.GeneralSystemDTO;
import cn.gov.forestry.common.domain.dto.metadata.MetadataFieldDTO;
import cn.gov.forestry.common.domain.dto.metadata.MetadataTableDTO;
import cn.gov.forestry.common.domain.dto.metadata.batch.MetadataFieldBatchDTO;
import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobDTO;
import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobLogDTO;
import cn.gov.forestry.common.file.FileContent;
import cn.gov.forestry.common.geojson.feature.FeatureTypeEnum;
import cn.gov.forestry.common.utils.CaseUtil;
import cn.gov.forestry.common.utils.ChineseUtils;
import cn.gov.forestry.common.uuid.UuidCreator;
import cn.gov.forestry.executor.client.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.awt.geom.AffineTransform;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
@Slf4j
public class ScheduleJobExecutor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FeatureJSON featureJSON = new FeatureJSON();

    private final GeneralInnerQueryClient generalInnerQueryClient;
    private final AssetsInnerResourceClient assetsInnerResourceClient;
    private final DatabaseInnerCRUDClient databaseInnerCRUDClient;
    private final MetadataInnerQueryClient metadataInnerQueryClient;
    private final MetadataInnerOptClient metadataInnerOptClient;
    private final ScheduleInnerJobClient scheduleInnerJobClient;

    public ScheduleJobExecutor(GeneralInnerQueryClient generalInnerQueryClient, AssetsInnerResourceClient assetsInnerResourceClient, DatabaseInnerCRUDClient databaseInnerCRUDClient, MetadataInnerQueryClient metadataInnerQueryClient, MetadataInnerOptClient metadataInnerOptClient, ScheduleInnerJobClient scheduleInnerJobClient) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.generalInnerQueryClient = generalInnerQueryClient;
        this.metadataInnerQueryClient = metadataInnerQueryClient;
        this.metadataInnerOptClient = metadataInnerOptClient;
        this.assetsInnerResourceClient = assetsInnerResourceClient;
        this.databaseInnerCRUDClient = databaseInnerCRUDClient;
        this.scheduleInnerJobClient = scheduleInnerJobClient;
    }

    public void execute(ScheduleJobDTO jobDTO) {
        LOGGER.info("ScheduleJobExecutor-received-job-[{}]-execute...", jobDTO.getId());
        // update job status
        updateJobStatus(jobDTO.getSystemId(), jobDTO.getId(), SystemScheduleJobStatusEnum.RUNNING);
        updateJobStartTime(jobDTO.getSystemId(), jobDTO.getId());
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute:job received");
        try {
            if (SystemScheduleJobTypeEnum.isImportExcel(jobDTO.getJobType())) {
                executeImportExcelJob(jobDTO);
            } else if (SystemScheduleJobTypeEnum.isImportShapefile(jobDTO.getJobType())) {
                executeImportShapefileJob(jobDTO);
            } else if (SystemScheduleJobTypeEnum.isImportGeoTiff(jobDTO.getJobType())) {
                executeImportGeoTiffJob(jobDTO);
            } else {
                saveLog(jobDTO, GeneralLogLevelEnum.ERROR, "execute:job failed");
                LOGGER.info("ScheduleJobExecutor-UNKNOW-job-[{}]-type", jobDTO.getId());
            }
            LOGGER.info("ScheduleJobExecutor-execute-job-[{}]-SUCCESS", jobDTO.getId());
            updateJobStatus(jobDTO.getSystemId(), jobDTO.getId(), SystemScheduleJobStatusEnum.SUCCESS);
            saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute:job success");
        } catch (Exception e) {
            LOGGER.error("ScheduleJobExecutor-execute-job-[{}]-ERROR", jobDTO.getId(), e);
            updateJobStatus(jobDTO.getSystemId(), jobDTO.getId(), SystemScheduleJobStatusEnum.ERROR);
            Map<String, Object> jobResult = new HashMap<>();
            jobResult.put("error", e);
            updateJobResult(jobDTO.getSystemId(), jobDTO.getId(), jobResult);
            saveLog(jobDTO, GeneralLogLevelEnum.ERROR, "execute:job failed:" + e.getMessage());
        }
        updateJobEndTime(jobDTO.getSystemId(), jobDTO.getId());
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute:job stoped");
    }

    private void executeImportExcelJob(ScheduleJobDTO jobDTO) throws Exception {
        LOGGER.info("ScheduleJobExecutor-executeImportExcelJob-running-at-offset-[{}]...", jobDTO.getJobTaskOffset());
        int preOffset = Math.toIntExact(jobDTO.getJobTaskOffset());
        Map<String, Object> params = jobDTO.getJobParams();

        String systemId = (String) params.get("systemId");
        String tableId = (String) params.get("tableId");
        // 系统信息
        GeneralSystemDTO generalSystemDTO = getGeneralSystemDTO(systemId);
        // 数据库连接
        DatabaseInfo databaseInfo = DatabaseInfo.fromSystemInfo(generalSystemDTO);
        // 表本身的信息
        MetadataTableDTO metadataTableInfo = getMetadataTableDTO(systemId, tableId);
        // 表对应的字段信息
        List<MetadataFieldDTO> metadataFieldListByTable = getMetadataFieldDTOList(systemId, tableId);

        FileContent excelFile = objectMapper.readValue(params.get("resourceFile").toString(), FileContent.class);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-excel:read excel file source");
        FileContent excelFileContent = assetsInnerResourceClient.getResourceFile(excelFile);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-excel:get excel file");
        DataFormatter formatter = new DataFormatter();
        //List<Map<String, Object>> excelContentMapList = exchangeInnerTransferClient.transferExcelToList(excelFileContent);
        // 读取整个Excel
        Workbook sheets = new XSSFWorkbook(new ByteArrayInputStream(excelFileContent.getBytes()));
        // 获取第一个表单Sheet
        Sheet sheetAt = sheets.getSheetAt(0);

        //默认第一行为标题行，i = 0
        Row titleRow = sheetAt.getRow(0);
        // 更新任务总数
        int excelRowCount = sheetAt.getPhysicalNumberOfRows();
        // 第二行是中文描述 从第三行开始,下标为2,并修正任务初始偏移量(跳过第一条) 任务总数-2
        int taskCount = excelRowCount - 2;
        updateJobTaskCount(jobDTO.getSystemId(), jobDTO.getId(), taskCount);
        saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-excel:task count:" + taskCount);
        // 批量插入的阈值
        int threshold = calculateThreshold(taskCount);
        saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-excel:sync threshold:" + threshold);
        // 缓存待插入的数据
        List<Map<String, FieldValue>> buffer = new ArrayList<>(threshold);
        // 第二行是中文描述 从第三行开始,下标为2
        int offset = 2;
        // 循环还是要所有数据全读
        for (int i = offset; i < excelRowCount; i++) {
            // 从之前进度继续执行
            if(offset > preOffset) {
                Row row = sheetAt.getRow(i);
                Map<String, Object> map = new HashMap<>();
                // 读取每一格内容
                for (int index = 0; index < row.getLastCellNum(); index++) {
                    Cell titleCell = titleRow.getCell(index);
                    Cell cell = row.getCell(index);
                    if (Objects.isNull(cell)) {
                        continue;
                    }
                    // 使用 DataFormatter 获取格式化后的字符串值，不会改变单元格本身
                    // DataFormatter.formatCellValue(cell) 会根据单元格的实际类型和格式返回最合适的字符串表示，包括日期、数字、布尔值等，无需手动 setCellType。
                    String cellValue = formatter.formatCellValue(cell);
                    if (cellValue.isEmpty()) {
                        continue;
                    }
                    map.put(getString(titleCell), cellValue);
                }
                if (!map.isEmpty()) {
                    // excel无法处理-第一条记录落库之前先检查一下是否已经创建了相关字段,如果没有就自动创建字段
                    // 只能严格根据字段设置从excel里读取

                    // 将请求转置为插入数据库记录,校验,填写默认值
                    Map<String, FieldValue> insertDoc = new HashMap<>();
                    metadataFieldListByTable.forEach(metadataFieldDTO -> {
                        // TODO 必填校验
                        // 新增的值
                        Object createValue = map.get(metadataFieldDTO.getFieldName());
                        // 获取字段声明的dataType
                        SystemFieldDataTypeEnum dataTypeEnum = SystemFieldDataTypeEnum.getByCodeOrDefault(metadataFieldDTO.getFieldDataType());
                        // 构造新增落库的实例
                        insertDoc.put(metadataFieldDTO.getFieldName(), FieldValueBuilder.createFieldValue(dataTypeEnum, createValue));
                        // 审计日志-create
                        //this.auditCreateAction(userRealmInfo, databaseInfo, metadataFieldDTO, createValue);
                        //LOGGER.debug("convert-excel-FieldDataType-{}-createValue-{}", dataTypeEnum.getCode(), createValue);
                    });
                    // 然后再赋值ID 避免被空值覆盖
                    UUID uuid = UuidCreator.getTimeOrderedEpoch();
                    insertDoc.put(SystemBuildInFieldEnum.ID.getFieldName(), FieldValueBuilder.string(uuid.toString()));
                    // 执行落库 改为批量创建
                    //String insertId = insert(databaseInfo, metadataTableInfo.getTableEntityName(), insertDoc);

                    //LOGGER.debug("excel-insert-id-{}", insertId);
                    buffer.add(insertDoc);
                }
                // 达到阈值，执行批量插入
                if (buffer.size() >= threshold) {
                    List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
                    LOGGER.debug("batch-insert-size: {}", insertedIds.size());
                    buffer.clear();
                    int taskOffset = offset - 2 + 1;
                    updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
                    saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-excel:buffer batch sync offset:" + taskOffset);
                }
            }
            offset++;
        }
        // 插入剩余不足一批的数据
        if (!buffer.isEmpty()) {
            List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
            LOGGER.debug("batch-insert-final-size: {}", insertedIds.size());
            buffer.clear();
            // 最终更新偏移量（不用再+1了）
            int taskOffset = offset - 2;
            updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-excel:last buffer batch sync offset:" + offset);
        }
    }

    private void executeImportShapefileJob(ScheduleJobDTO jobDTO) throws Exception {
        LOGGER.info("ScheduleJobExecutor-executeImportShapefileJob-running-at-offset-[{}]...", jobDTO.getJobTaskOffset());
        int preOffset = Math.toIntExact(jobDTO.getJobTaskOffset());
        Map<String, Object> params = jobDTO.getJobParams();

        String systemId = (String) params.get("systemId");
        String tableId = (String) params.get("tableId");
        // 系统信息
        GeneralSystemDTO generalSystemDTO = getGeneralSystemDTO(systemId);
        // 数据库连接
        DatabaseInfo databaseInfo = DatabaseInfo.fromSystemInfo(generalSystemDTO);
        // 表本身的信息
        MetadataTableDTO metadataTableInfo = getMetadataTableDTO(systemId, tableId);

        FileContent shapeFile = objectMapper.readValue(params.get("resourceFile").toString(), FileContent.class);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-shapefile:read shapefile source");
        FileContent shapeFileContent = assetsInnerResourceClient.getResourceFile(shapeFile);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-shapefile:get shapefile");

        // 创建临时文件和目录
        File tempZip = null;
        Path extractDir = null;
        try {
            // 保存临时文件
            tempZip = File.createTempFile("shapefile", ".zip");
            try (FileOutputStream fos = new FileOutputStream(tempZip)) {
                fos.write(shapeFileContent.getBytes());
            }
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:create temp zip file");
            // 解压 ZIP 文件
            extractDir = Files.createTempDirectory("shapefile-extract-");
            unzip(tempZip, extractDir);
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:unzip temp zip file");

            // 获取实际解压路径（可能包含同名子目录）
            Path actualExtractDir = findActualExtractDir(extractDir);

            if (shapefileIsValidated(actualExtractDir)) {
                // 处理 Shapefile 文件
                for (File file : extractDir.toFile().listFiles()) {
                    if (file.getName().endsWith(".shp")) {
                        saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:find .shp file");
                        File shapefile = file;
                        ShapefileDataStore shpDataStore = new ShapefileDataStore(shapefile.toURI().toURL());
                        SimpleFeatureIterator iterator = null;
                        //设置编码
                        try{
                            shpDataStore.setCharset(StandardCharsets.UTF_8);
                            String featureSourceName = shpDataStore.getSchema().getTypeName();
                            String typeName = shpDataStore.getSchema().getGeometryDescriptor().getType().getBinding().getTypeName();
                            FeatureTypeEnum featureTypeEnum = FeatureTypeEnum.byJtsTypeName(typeName);
                            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:read shapefile feature source");
                            if (!ObjectUtils.isEmpty(featureTypeEnum)) {
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:read shapefile feature type" + featureTypeEnum.getTypeName());
                                SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(featureSourceName);
                                SimpleFeatureCollection featuresResult = featureSource.getFeatures();
                                // taskCount 是基于不可靠的 size() 得到的，后续进度条、任务数等都可能出错。
                                // 必须先遍历一次，再缓存到内存中
                                List<SimpleFeature> featureList = new ArrayList<>();
                                Envelope envelope = new Envelope(); // 用于存储边界框
                                int taskCount = 0;
                                iterator = featuresResult.features();
                                while (iterator.hasNext()) {
                                    SimpleFeature feature = iterator.next();
                                    featureList.add(feature);
                                    taskCount++;
                                    // 更新边界框
                                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                                    if (!ObjectUtils.isEmpty(geometry)) {
                                        envelope.expandToInclude(geometry.getEnvelopeInternal());
                                    }
                                }
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:read shapefile feature count:" + taskCount);
                                // 获取坐标参考系统
                                CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
                                String crsWkt = crs != null ? crs.toWKT() : "";
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:read shapefile feature crs wkt:" + crsWkt);
                                Integer epsgCode = null;
                                if (crs != null) {
                                    try {
                                        epsgCode = CRS.lookupEpsgCode(crs, true); // 尝试获取 EPSG 编码
                                        saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:read shapefile feature epsg code:" + epsgCode);
                                    } catch (Exception e) {
                                        // 如果无法找到EPSG编码，则不添加该属性或记录日志
                                    }
                                }
                                // 更新shapefile元数据
                                Map<String, Object> shapeFileMetadataProperties = new HashMap<>();
                                // 组装元数据
                                // 图像基本信息
                                shapeFileMetadataProperties.put("featureCount", taskCount);

                                // 坐标参考系统
                                shapeFileMetadataProperties.put("crsWkt", crsWkt);
                                if (epsgCode != null) {
                                    shapeFileMetadataProperties.put("epsgCode", epsgCode);
                                }
                                shapeFileMetadataProperties.put(SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName(), makeBbox(envelope));
                                // 保存元数据,包裹一层key
                                Map<String, Object> metadataTableAdditionalPropertiesProperties = new HashMap<>();
                                metadataTableAdditionalPropertiesProperties.put(SystemTableAdditionalPropertiesKeyEnum.SHAPE_FILE_METADATA_PROPERTIES.getKey(), shapeFileMetadataProperties);
                                updateTableAdditionalProperties(systemId, tableId, metadataTableAdditionalPropertiesProperties);
                                saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-shapefile:update table metadata");
                                // 更新任务状态和任务数量以及进度
                                updateJobTaskCount(jobDTO.getSystemId(), jobDTO.getId(), taskCount);
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:task count:" + taskCount);
                                // 批量插入的阈值
                                int threshold = calculateThreshold(taskCount);
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:sync threshold:" + threshold);
                                // 缓存待插入的数据
                                List<Map<String, FieldValue>> buffer = new ArrayList<>(threshold);

                                //遍历feature转为json对象
                                int offset = 0;
                                for (SimpleFeature simpleFeature : featureList) {
                                    if (offset > preOffset) {
                                        Map<String, Object> featureObjectMap = this.makeFeatureObjectMap(simpleFeature);
                                        if (!ObjectUtils.isEmpty(featureObjectMap)) {

                                            // 落库
                                            HashMap<String, Object> properties = (HashMap<String, Object>) featureObjectMap.get("properties");
                                            // 中文字段转拼音字段
                                            Map<String, Object> convertedProperties = ChineseUtils.convertKeysToPinyinWithUniqueSuffix(properties);
                                            Map<String, FieldValue> fieldValueMap = FieldValueBuilder.convertObjectMap(convertedProperties);
                                            // 字段转小写加下划线
                                            HashMap<String, FieldValue> propertiesSnakeCase = CaseUtil.convertKeysToSnakeCase(fieldValueMap);
                                            // 主键
                                            UUID uuid = UuidCreator.getTimeOrderedEpoch();
                                            propertiesSnakeCase.put(SystemBuildInFieldEnum.ID.getFieldName(), FieldValueBuilder.string(uuid.toString()));
                                            // 第一条记录落库之前先检查一下是否已经创建了相关字段,如果没有就自动创建字段
                                            if (offset == 0) {
                                                MetadataFieldBatchDTO fieldBatch = createFieldBatch(systemId, tableId, propertiesSnakeCase);
                                                LOGGER.info("ScheduleJobExecutor-executeImportShapefileJob-auto-create-field-[{}]...", fieldBatch.getCreatedCount());
                                                saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-shapefile:created field:" + fieldBatch.getCreatedCount());
                                            }
                                            // 追加空间字段值
                                            propertiesSnakeCase.put(
                                                    SystemBuildInFieldEnum.GEOMETRY.getFieldName(),
                                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY.getFieldName()))
                                            );
                                            propertiesSnakeCase.put(
                                                    SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName(),
                                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName()))
                                            );
                                            propertiesSnakeCase.put(
                                                    SystemBuildInFieldEnum.GEOMETRY_AREA.getFieldName(),
                                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY_AREA.getFieldName()))
                                            );

                                            //propertiesSnakeCase.

                                            // 将请求转置为插入数据库记录,校验,填写默认值
//                                            Map<String, Object> insertDoc = new HashMap<>();
//                                            metadataFieldListByTable.forEach(metadataFieldDTO -> {
//                                                // 新增的值
//                                                Object createValue = propertiesSnakeCase.get(metadataFieldDTO.getFieldName());
//                                                // 构造新增落库的实例
//                                                insertDoc.put(metadataFieldDTO.getFieldName(), createValue);
//                                                // 审计日志-create
//                                                //this.auditCreateAction(userRealmInfo, databaseInfo, metadataFieldDTO, createValue);
//                                            });
                                            // 执行落库
                                            //String insertId = insert(databaseInfo, metadataTableInfo.getTableEntityName(), insertDoc);
                                            //LOGGER.debug("shapefile-insert-id-{}", insertId);
                                            // 不根据field字段创建
                                            //改为批量创建
                                            buffer.add(propertiesSnakeCase);
                                        }
                                        // 达到阈值，执行批量插入
                                        if (buffer.size() >= threshold) {
                                            List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
                                            //LOGGER.debug("batch-insert-size: {}", insertedIds.size());
                                            buffer.clear();
                                            int taskOffset = offset + 1;
                                            updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
                                            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:buffer batch sync offset:" + taskOffset);
                                        }
                                    }
                                    offset++;
                                }
                                // 插入剩余不足一批的数据
                                if (!buffer.isEmpty()) {
                                    List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
                                    LOGGER.debug("ScheduleJobExecutor-executeImportShapefileJob-batch-insert-final-size:[{}]", insertedIds.size());
                                    buffer.clear();
                                    // 最终更新偏移量（不用再+1了）
                                    int taskOffset = offset;
                                    updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
                                    saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:last buffer batch sync offset:" + taskOffset);
                                }
                                featureList.clear();
                                iterator.close();
                            } else {
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-shapefile:unknow shapefile feature source");
                            }
                        } catch (Exception e) {
                            LOGGER.error("ExchangeShapefile-import-ERROR", e);
                            saveLog(jobDTO, GeneralLogLevelEnum.ERROR, "execute-shapefile:error" + e.getMessage());
                            throw new RuntimeException(e);
                        } finally {
                            // 确保迭代器和数据源都被关闭
                            if (!ObjectUtils.isEmpty(iterator)) {
                                iterator.close();
                            }
                            if (!ObjectUtils.isEmpty(shpDataStore)) {
                                shpDataStore.dispose(); // 或者 close()，两者都可以
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("ExchangeShapefile-import-ERROR", e);
            saveLog(jobDTO, GeneralLogLevelEnum.ERROR, "execute-shapefile:error" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // 清理临时文件和目录
            cleanup(tempZip, extractDir);
        }
    }

    private void executeImportGeoTiffJob(ScheduleJobDTO jobDTO) throws Exception {
        LOGGER.info("ScheduleJobExecutor-executeImportGeoTiffJob-running-at-offset-[{}]...", jobDTO.getJobTaskOffset());
        int preOffset = Math.toIntExact(jobDTO.getJobTaskOffset());
        Map<String, Object> params = jobDTO.getJobParams();

        String systemId = (String) params.get("systemId");
        String tableId = (String) params.get("tableId");
        // 系统信息
        GeneralSystemDTO generalSystemDTO = getGeneralSystemDTO(systemId);
        // 数据库连接
        DatabaseInfo databaseInfo = DatabaseInfo.fromSystemInfo(generalSystemDTO);
        // 表本身的信息
        MetadataTableDTO metadataTableInfo = getMetadataTableDTO(systemId, tableId);

        FileContent geoTiffFile = objectMapper.readValue(params.get("resourceFile").toString(), FileContent.class);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-geo-tiff:get tiff resource");
        FileContent geoTiffFileContent = assetsInnerResourceClient.getResourceFile(geoTiffFile);
        saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-geo-tiff:get tiff file");
        GeoTiffReader reader = null;
        try {
            // 打开GeoTIFF文件
            reader = new GeoTiffReader(new ByteArrayInputStream(geoTiffFileContent.getBytes()));
            GridCoverage2D coverage = reader.read(null);
            // 获取坐标参考系统
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
            String crsWkt = crs.toWKT(); // WKT 格式表示
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:read tiff crs wkt:" + crsWkt);
            // 获取栅格数据
            RenderedImage image = coverage.getRenderedImage();
            Raster raster = image.getData();
            int width = image.getWidth();
            int height = image.getHeight();
            int bandCount = raster.getNumBands();
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:read tiff raster band:" + bandCount);
            //LOGGER.info("图像信息 - 宽度: {}, 高度: {}, 波段数: {}", width, height, bandCount);
            // 获取第1波段的NoData值
            double[] noDataValues = coverage.getSampleDimension(0).getNoDataValues();
//            LOGGER.info("noDataValues: {}", Arrays.toString(noDataValues));
            // 获取仿射变换参数（像素到地理坐标的转换）
            AffineTransform gridToCRS = (AffineTransform) coverage.getGridGeometry().getGridToCRS();
            // 像元宽高
            double pixelWidth = gridToCRS.getScaleX();
            double pixelHeight = Math.abs(gridToCRS.getScaleY());
            double rotationX = gridToCRS.getShearX();
            double rotationY = gridToCRS.getShearY();
            double minX = gridToCRS.getTranslateX();
            double maxY = gridToCRS.getTranslateY();
            double maxX = minX + pixelWidth * width;
            double minY = maxY - pixelHeight * height;

            // 获取NoData值（适用于每个波段）
//            List<Double> noDataValuesList = new ArrayList<>();
//            for (int i = 0; i < bandCount; i++) {
//                double[] noDataValues = coverage.getSampleDimension(i).getNoDataValues();
//                if (noDataValues != null && noDataValues.length > 0) {
//                    noDataValuesList.add(noDataValues[0]);
//                } else {
//                    noDataValuesList.add(Double.NaN);
//                }
//            }

            // 更新tif元数据
            Map<String, Object> tifMetadataProperties = new HashMap<>();
            // 图像基本信息
            tifMetadataProperties.put("rasterWidth", width);
            tifMetadataProperties.put("rasterHeight", height);
            tifMetadataProperties.put("rasterNumBands", bandCount);
            // 坐标参考系统
            tifMetadataProperties.put("crsWkt", crsWkt);
            try {
                Integer epsgCode = CRS.lookupEpsgCode(crs, true); // 尝试获取 EPSG 编码
                tifMetadataProperties.put("epsgCode", epsgCode);
            } catch (Exception e) {
                // 如果无法找到EPSG编码，则不添加该属性或记录日志
            }
            // 仿射变换参数
            tifMetadataProperties.put("pixelWidth", pixelWidth);
            tifMetadataProperties.put("pixelHeight", pixelHeight);
            tifMetadataProperties.put("rotationX", rotationX);
            tifMetadataProperties.put("rotationY", rotationY);

            // 图像边界范围（地理范围）必须使用geojson的bbox规范minx miny maxx maxy
            tifMetadataProperties.put(SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName(), makeBbox(minX, minY, maxX, maxY));

            // NoData 值（每个波段）
            //tifMetadataProperties.put("nodataValues", noDataValuesList.toArray());

            // Raster 附加信息（如你已有）
            tifMetadataProperties.put("rasterMinX", raster.getMinX());
            tifMetadataProperties.put("rasterMinY", raster.getMinY());
            tifMetadataProperties.put("rasterSampleModelTranslateX", raster.getSampleModelTranslateX());
            tifMetadataProperties.put("rasterSampleModelTranslateY", raster.getSampleModelTranslateY());
            tifMetadataProperties.put("rasterNumDataElements", raster.getNumDataElements());

            // 保存元数据,包裹一层key
            Map<String, Object> metadataTableAdditionalPropertiesProperties = new HashMap<>();
            metadataTableAdditionalPropertiesProperties.put(SystemTableAdditionalPropertiesKeyEnum.TIF_METADATA_PROPERTIES.getKey(), tifMetadataProperties);
            updateTableAdditionalProperties(systemId, tableId, metadataTableAdditionalPropertiesProperties);
            saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-geo-tiff:update table metadata");
            // 保存元数据完成
            // 定义要素结构
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            builder.setName("TiffPixel");
            builder.setCRS(crs);
            builder.add("the_geom", Polygon.class);
//            builder.add("col", Integer.class);
//            builder.add("row", Integer.class);
            for (int i = 0; i < bandCount; i++) {
                builder.add("band_" + (i + 1), Double.class);
            }
            SimpleFeatureType featureType = builder.buildFeatureType();
            // 定义要素容器
            //List<SimpleFeature> features = new ArrayList<>();
            // 初始化要素构建器
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            // 定义几何工厂
            GeometryFactory geometryFactory = new GeometryFactory();

            // 总数
            int taskCount = height * width;
            // 更新任务状态和任务数量以及进度
            updateJobTaskCount(jobDTO.getSystemId(), jobDTO.getId(), taskCount);
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:task count:" + taskCount);
            // 批量插入的阈值
            int threshold = calculateThreshold(taskCount);
            saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:sync threshold:" + threshold);
            // 缓存待插入的数据
            List<Map<String, FieldValue>> buffer = new ArrayList<>(threshold);
            // 遍历像元
            int offset = 0;
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (offset > preOffset) {
                        double[] pixelValues = new double[bandCount];
                        boolean isNoData = false;
                        for (int band = 0; band < bandCount; band++) {
                            pixelValues[band] = raster.getSampleDouble(col, row, band);
                            if (noDataValues != null) {
                                for (double noData : noDataValues) {
                                    if (Double.compare(pixelValues[band], noData) == 0) {
                                        isNoData = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (!isNoData) {
                            // 像元左上角坐标（经度，纬度）
                            DirectPosition2D cornerPos = new DirectPosition2D(col - 0.5, row - 0.5);
                            gridToCRS.transform(cornerPos, cornerPos);
                            // 创建像元对应的矩形多边形
                            Coordinate[] coords = new Coordinate[5];
                            // 左上角-右上-右下-左下-左上
                            coords[0] = new Coordinate(cornerPos.getX(), cornerPos.getY());
                            coords[1] = new Coordinate(cornerPos.getX() + pixelWidth, cornerPos.getY());
                            coords[2] = new Coordinate(cornerPos.getX() + pixelWidth, cornerPos.getY() - pixelHeight);
                            coords[3] = new Coordinate(cornerPos.getX(), cornerPos.getY() - pixelHeight);
                            coords[4] = coords[0];
                            // 创建多边形
                            Polygon polygon = geometryFactory.createPolygon(geometryFactory.createLinearRing(coords), null);
                            // 构建要素并添加属性
                            featureBuilder.reset();
                            featureBuilder.add(polygon);
//                    featureBuilder.add(col);
//                    featureBuilder.add(row);
                            for (double v : pixelValues) {
                                featureBuilder.add(v);
                            }
                            SimpleFeature feature = featureBuilder.buildFeature(null);
                            Map<String, Object> featureObjectMap = makeFeatureObjectMap(feature);
                            HashMap<String, Object> properties = (HashMap<String, Object>) featureObjectMap.get("properties");
                            // 中文字段转拼音字段
                            Map<String, Object> convertedProperties = ChineseUtils.convertKeysToPinyinWithUniqueSuffix(properties);
                            Map<String, FieldValue> fieldValueMap = FieldValueBuilder.convertObjectMap(convertedProperties);
                            // 字段转小写加下划线
                            HashMap<String, FieldValue> propertiesSnakeCase = CaseUtil.convertKeysToSnakeCase(fieldValueMap);

                            // 第一条记录落库之前先检查一下是否已经创建了相关字段,如果没有就自动创建字段
                            if (offset == 0) {
                                MetadataFieldBatchDTO fieldBatch = createFieldBatch(systemId, tableId, propertiesSnakeCase);
                                LOGGER.info("ScheduleJobExecutor-executeImportGeoTiffJob-auto-create-field-[{}]...", fieldBatch.getCreatedCount());
                                saveLog(jobDTO, GeneralLogLevelEnum.INFO, "execute-geo-tiff:created field:" + fieldBatch.getCreatedCount());
                            }
                            // 空间修复?入库时应完整存储原始空间数据还是修复后的数据?2025-05-11还是存放原始数据 2025-05-14数据库应该放原始数据
                            //propertiesSnakeCase.put(FeatureKeyEnum.BBOX.getKey(), feature.get(FeatureKeyEnum.BBOX.getKey()));
                            propertiesSnakeCase.put(
                                    SystemBuildInFieldEnum.GEOMETRY.getFieldName(),
                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY.getFieldName()))
                            );
                            propertiesSnakeCase.put(
                                    SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName(),
                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName()))
                            );
                            propertiesSnakeCase.put(
                                    SystemBuildInFieldEnum.GEOMETRY_AREA.getFieldName(),
                                    FieldValueBuilder.object(featureObjectMap.get(SystemBuildInFieldEnum.GEOMETRY_AREA.getFieldName()))
                            );

                            // 主键
                            UUID uuid = UuidCreator.getTimeOrderedEpoch();
                            propertiesSnakeCase.put(SystemBuildInFieldEnum.ID.getFieldName(), FieldValueBuilder.string(uuid.toString()));
                            // 将请求转置为插入数据库记录,校验,填写默认值
//                            Map<String, Object> insertDoc = new HashMap<>();
//                            metadataFieldListByTable.forEach(metadataFieldDTO -> {
//                                // TODO 必填校验
//                                // 新增的值
//                                Object createValue = propertiesSnakeCase.get(metadataFieldDTO.getFieldName());
//                                // 构造新增落库的实例
//                                insertDoc.put(metadataFieldDTO.getFieldName(), createValue);
//                                // 审计日志-create
//                                //this.auditCreateAction(userRealmInfo, databaseInfo, metadataFieldDTO, createValue);
//                            });

                            // 执行落库
                            //String insertId = insert(databaseInfo, metadataTableInfo.getTableEntityName(), insertDoc);
                            //LOGGER.debug("geoTiff-insert-id-{}", insertId);
                            // 不根据field来创建了
                            // 改为批量插入
                            // 加入缓冲区
                            buffer.add(propertiesSnakeCase);

                            // 达到阈值则批量插入
                            if (buffer.size() >= threshold) {
                                List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
                                //LOGGER.debug("batch-insert-size: {}", insertedIds.size());

                                buffer.clear();
                                int taskOffset = offset + 1;
                                updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
                                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:buffer batch sync offset:" + taskOffset);
                            }
                        }
                    }
                    //updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), offset + 1);
                    offset++;
                }
            }
            // 插入剩余不足一批的数据
            if (!buffer.isEmpty()) {
                List<String> insertedIds = insertBatch(databaseInfo, metadataTableInfo.getTableEntityName(), buffer);
                LOGGER.debug("ScheduleJobExecutor-executeImportGeoTiffJob-batch-insert-final-size:[{}]", insertedIds.size());
                buffer.clear();
                // 最终更新偏移量（不用再+1了）
                int taskOffset = offset;
                updateJobTaskOffset(jobDTO.getSystemId(), jobDTO.getId(), taskOffset);
                saveLog(jobDTO, GeneralLogLevelEnum.DEBUG, "execute-geo-tiff:last buffer batch sync offset:" + taskOffset);
            }
        } catch (Exception e) {
            LOGGER.error("ExchangeGeoTiff-readGeoTiff-ERROR", e);
            saveLog(jobDTO, GeneralLogLevelEnum.ERROR, "execute-geotiff:error:" + e.getMessage());
        } finally {
            if (!ObjectUtils.isEmpty(reader)) {
                reader.dispose();
            }
        }
    }
    /**
     * 把单元格的内容转为字符串
     *
     * @param cell 单元格
     * @return String
     */
    private String getString(Cell cell) {
        if (cell == null) {
            return "";
        }
//        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//            return String.valueOf(cell.getNumericCellValue());
//        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
//            return String.valueOf(cell.getBooleanCellValue());
//        } else {
//            return cell.getStringCellValue();
//        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }
    // shapefile
    // 验证解压后的文件
    private Boolean shapefileIsValidated(Path actualExtractDir) {
        // 验证解压后的文件
        boolean hasShp = false;
        boolean hasDbf = false;
        for (File extractedFile : actualExtractDir.toFile().listFiles()) {
            if (extractedFile.getName().endsWith(".shp")) {
                hasShp = true;
            }
            if (extractedFile.getName().endsWith(".dbf")) {
                hasDbf = true;
            }
        }

        if (!hasShp || !hasDbf) {
            LOGGER.error("importShapefile-no-SHP-or-DBF");
            return false;
        }
        return true;
    }
    // 解压 ZIP 文件
    private void unzip(File zipFile, Path outputDir) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path entryPath = outputDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zip.getInputStream(entry), entryPath);
                }
            }
        }
    }
    // 查找实际解压路径（可能包含同名子目录）
    private Path findActualExtractDir(Path extractDir) throws IOException {
        File[] files = extractDir.toFile().listFiles();
        if (files == null || files.length == 0) {
            throw new IOException("解压目录为空！");
        }

        // 如果只有一个子目录，则返回该子目录的路径
        if (files.length == 1 && files[0].isDirectory()) {
            return files[0].toPath();
        }

        // 否则直接返回解压目录
        return extractDir;
    }
    // 清理临时文件和目录
    private void cleanup(File tempZip, Path extractDir) {
        if (tempZip != null && tempZip.exists()) {
            if (!tempZip.delete()) {
                LOGGER.error("ExchangeShapefile-cleanup-ERROR-[{}]", tempZip.getAbsolutePath());
            }
        }

        if (extractDir != null && Files.exists(extractDir)) {
            try {
                deleteDirectoryRecursively(extractDir);
            } catch (IOException e) {
                LOGGER.error("ExchangeShapefile-cleanup-ERROR-[{}]", extractDir, e);
            }
        }
    }
    // 递归删除目录及其内容
    private void deleteDirectoryRecursively(Path dir) throws IOException {
        Files.walk(dir)
                .sorted((p1, p2) -> -p1.compareTo(p2)) // 按逆序排序，先删除子文件/子目录
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        LOGGER.error("ExchangeShapefile-deleteDirectoryRecursively-ERROR-[{}]", path, e);
                    }
                });
    }
    private Map<String, Object> makeFeatureObjectMap(SimpleFeature feature) throws IOException {
        StringWriter writer = new StringWriter();
        try {
            Geometry geometry = (Geometry) feature.getDefaultGeometry();

            featureJSON.writeFeature(feature, writer);
            String featureGeojson = writer.toString();
            Map<String, Object> featureObject = objectMapper.readValue(featureGeojson, HashMap.class);
            //featureObject.put(FeatureKeyEnum.BBOX.getKey(), makeBbox(geometry.getEnvelopeInternal()));
            featureObject.put(SystemBuildInFieldEnum.GEOMETRY_BBOX.getFieldName(), makeBbox(geometry.getEnvelopeInternal()));
            featureObject.put(SystemBuildInFieldEnum.GEOMETRY_AREA.getFieldName(), geometry.getArea());
            return featureObject;
        } catch (IOException e) {
            LOGGER.error("ExchangeShapefile-makeFeatureObjectMap-ERROR", e);
        }
        finally {
            writer.close();
        }
        return new HashMap<>();
    }
    //
    // minx miny maxx maxy
    // minLng minLat maxLng maxLat
    // 113.748062133789 36.4743270874023 114.377662658691 37.0211868286133
    // bbox BOX(114.172345546508 36.69386198076,114.201141681071 36.7047419078144)
    private List<Double> makeBbox(BoundingBox boundingBox) {
        return Arrays.asList(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMaxX(), boundingBox.getMaxY());
    }
    public List<Double> makeBbox(Envelope envelopeInternal) {
        return Arrays.asList(envelopeInternal.getMinX(), envelopeInternal.getMinY(), envelopeInternal.getMaxX(), envelopeInternal.getMaxY());
    }
    private List<Double> makeBbox(Double minX, Double minY, Double maxX, Double maxY) {
        return Arrays.asList(minX, minY, maxX, maxY);
    }
    private void updateJobStatus(String systemId, String jobId, SystemScheduleJobStatusEnum jobStatus) {
        ScheduleJobDTO updateJobStatusParam = new ScheduleJobDTO();
        updateJobStatusParam.setSystemId(systemId);
        updateJobStatusParam.setId(jobId);
        updateJobStatusParam.setJobStatus(jobStatus.getCode());
        scheduleInnerJobClient.updateAsynchronousJob(updateJobStatusParam);
    }
    private void updateJobTaskCount(String systemId, String jobId, Integer taskCount) {
        ScheduleJobDTO updateJobTaskCountParam = new ScheduleJobDTO();
        updateJobTaskCountParam.setSystemId(systemId);
        updateJobTaskCountParam.setId(jobId);
        updateJobTaskCountParam.setJobTaskCount(Long.valueOf(taskCount));
        scheduleInnerJobClient.updateAsynchronousJob(updateJobTaskCountParam);
    }
    private void updateJobTaskOffset(String systemId, String jobId, Integer taskOffset) {
        ScheduleJobDTO updateJobTaskOffsetParam = new ScheduleJobDTO();
        updateJobTaskOffsetParam.setSystemId(systemId);
        updateJobTaskOffsetParam.setId(jobId);
        updateJobTaskOffsetParam.setJobTaskOffset(Long.valueOf(taskOffset));
        scheduleInnerJobClient.updateAsynchronousJob(updateJobTaskOffsetParam);
    }
    private int calculateThreshold(int taskCount) {
        if (taskCount < 100) {
            return 1;
        } else if (taskCount < 10_000) {
            return 10;
        } else if (taskCount < 1_000_000) {
            return 100;
        } else if (taskCount < 100_000_000) {
            return 1000;
        } else {
            return 10_000;
        }
    }
    private void updateJobStartTime(String systemId, String jobId) {
        ScheduleJobDTO updateJobTaskStartTimeParam = new ScheduleJobDTO();
        updateJobTaskStartTimeParam.setSystemId(systemId);
        updateJobTaskStartTimeParam.setId(jobId);
        updateJobTaskStartTimeParam.setJobStartTime(new Date());
        scheduleInnerJobClient.updateAsynchronousJob(updateJobTaskStartTimeParam);
    }
    private void updateJobEndTime(String systemId, String jobId) {
        ScheduleJobDTO updateJobTaskEndTimeParam = new ScheduleJobDTO();
        updateJobTaskEndTimeParam.setSystemId(systemId);
        updateJobTaskEndTimeParam.setId(jobId);
        updateJobTaskEndTimeParam.setJobEndTime(new Date());
        scheduleInnerJobClient.updateAsynchronousJob(updateJobTaskEndTimeParam);
    }
    private void updateJobResult(String systemId, String jobId, Map<String, Object> jobResult) {
        ScheduleJobDTO updateJobTaskEndTimeParam = new ScheduleJobDTO();
        updateJobTaskEndTimeParam.setSystemId(systemId);
        updateJobTaskEndTimeParam.setId(jobId);
        updateJobTaskEndTimeParam.setJobResult(jobResult);
        scheduleInnerJobClient.updateAsynchronousJob(updateJobTaskEndTimeParam);
    }
    private GeneralSystemDTO getGeneralSystemDTO(String systemId) {
        GeneralSystemDTO systemQuery = new GeneralSystemDTO();
        systemQuery.setId(systemId);
        return generalInnerQueryClient.getSystemInfo(systemQuery);
    }
    private MetadataTableDTO getMetadataTableDTO(String systemId, String tableId) {
        MetadataTableDTO tableQuery = new MetadataTableDTO();
        tableQuery.setSystemId(systemId);
        tableQuery.setId(tableId);
        return metadataInnerQueryClient.getMetadataTableInfo(tableQuery);
    }
    private List<MetadataFieldDTO> getMetadataFieldDTOList(String systemId, String tableId) {
        MetadataFieldDTO fieldQuery = new MetadataFieldDTO();
        fieldQuery.setSystemId(systemId);
        fieldQuery.setTableId(tableId);
        return metadataInnerQueryClient.getMetadataFieldListByTable(fieldQuery);
    }
    private List<String> insertBatch(DatabaseInfo databaseInfo, String tableEntityName, List<Map<String, FieldValue>> propertiesList) {
        InsertBatchParams insertBatchParams = new InsertBatchParams();
        insertBatchParams.setDatabaseInfo(databaseInfo);
        insertBatchParams.setTableEntityName(tableEntityName);
        insertBatchParams.setPropertiesList(propertiesList);
        return databaseInnerCRUDClient.insertBatch(insertBatchParams);
    }
    private MetadataTableDTO updateTableAdditionalProperties(String systemId, String tableId, Map<String, Object> properties) {
        MetadataTableDTO updateTableAdditionalPropertiesParams = new MetadataTableDTO();
        updateTableAdditionalPropertiesParams.setSystemId(systemId);
        updateTableAdditionalPropertiesParams.setId(tableId);
        updateTableAdditionalPropertiesParams.setAdditionalProperties(properties);
        return metadataInnerOptClient.updateMetadataTableAdditionalProperties(updateTableAdditionalPropertiesParams);
    }
    private MetadataFieldBatchDTO createFieldBatch(String systemId, String tableId, Map<String, FieldValue> properties) {
        MetadataFieldBatchDTO createFieldBatchParams = new MetadataFieldBatchDTO();
        createFieldBatchParams.setSystemId(systemId);
        createFieldBatchParams.setTableId(tableId);
        List<MetadataFieldDTO> fields = properties.keySet().stream().map(key -> {
            // @see DefaultFieldValueGenerator.generalNormalField()
            // 最少只需要一个fieldName
            MetadataFieldDTO field = new MetadataFieldDTO();
            field.setFieldName(key);
            return field;
        }).collect(Collectors.toList());
        createFieldBatchParams.setFields(fields);
        return metadataInnerOptClient.createFieldBatch(createFieldBatchParams);
    }
    private void saveLog(ScheduleJobDTO jobDTO, GeneralLogLevelEnum logLevel, String logContent) {
        ScheduleJobLogDTO jobLogDTO = new ScheduleJobLogDTO();
        jobLogDTO.setSystemId(jobDTO.getSystemId());
        jobLogDTO.setJobId(jobDTO.getId());
        jobLogDTO.setJobType(jobDTO.getJobType());
        // 暂时写死，后续会获取自己的主机名
        jobLogDTO.setJobExecutor("executor");
        jobLogDTO.setJobLogLevel(logLevel.getLevel());
        jobLogDTO.setJobLogContent(logContent);
        jobLogDTO.setJobLogTime(new Date());

        try {
            scheduleInnerJobClient.createScheduleJobLog(jobLogDTO);
        } catch (Exception e) {
            LOGGER.error("sync-log-error", e);
        }
    }
}
