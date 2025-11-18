package cn.gov.forestry.common.schedule;

import cn.gov.forestry.common.domain.bo.SystemScheduleJobStatusEnum;
import cn.gov.forestry.common.domain.bo.SystemScheduleJobTypeEnum;
import cn.gov.forestry.common.domain.dto.general.GeneralUserDTO;
import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobDTO;
import cn.gov.forestry.common.file.FileContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleJobBuilder {

    public static ScheduleJobDTO buildImportExcelJob(
            String systemId,
            String tableId,
            GeneralUserDTO createUser,
            FileContent excelFile
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleJobDTO job = new ScheduleJobDTO();
        job.setSystemId(systemId);
        job.setJobCreateUserId(createUser.getId());
        job.setJobCreateUserName(createUser.getUserName());
        job.setJobCreateTime(new Date());
        job.setJobType(SystemScheduleJobTypeEnum.IMPORT_EXCEL.getCode());
        job.setJobTaskCount(1L);
        // 保证继续任务时的进度和第一次下标为0时的处理
        job.setJobTaskOffset(-1L);
        job.setJobStatus(SystemScheduleJobStatusEnum.INIT.getCode());
        job.setJobMaxRetryTimes(3L);
        job.setJobCurrentRetryTimes(0L);
        Map<String, Object> jobParams = new HashMap<>();
        // 清空数据, 执行任务时重新获取原始数据流
        excelFile.setBytes(new byte[0]);
        jobParams.put("systemId", systemId);
        jobParams.put("tableId", tableId);
        try {
            jobParams.put("createUser", objectMapper.writeValueAsString(createUser));
            jobParams.put("resourceFile", objectMapper.writeValueAsString(excelFile));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        job.setJobParams(jobParams);
        return job;
    }

    public static ScheduleJobDTO buildImportShapefileJob(
            String systemId,
            String tableId,
            GeneralUserDTO createUser,
            FileContent shapeFile
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleJobDTO job = new ScheduleJobDTO();
        job.setSystemId(systemId);
        job.setJobCreateUserId(createUser.getId());
        job.setJobCreateUserName(createUser.getUserName());
        job.setJobCreateTime(new Date());
        job.setJobType(SystemScheduleJobTypeEnum.IMPORT_SHAPEFILE.getCode());
        job.setJobTaskCount(1L);
        // 保证继续任务时的进度和第一次下标为0时的处理
        job.setJobTaskOffset(-1L);
        job.setJobStatus(SystemScheduleJobStatusEnum.INIT.getCode());
        job.setJobMaxRetryTimes(3L);
        job.setJobCurrentRetryTimes(0L);
        Map<String, Object> jobParams = new HashMap<>();
        // 清空数据, 执行任务时重新获取原始数据流
        shapeFile.setBytes(new byte[0]);
        jobParams.put("systemId", systemId);
        jobParams.put("tableId", tableId);
        try {
            jobParams.put("createUser", objectMapper.writeValueAsString(createUser));
            jobParams.put("resourceFile", objectMapper.writeValueAsString(shapeFile));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        job.setJobParams(jobParams);
        return job;
    }

    public static ScheduleJobDTO buildImportGeoTiffJob(
            String systemId,
            String tableId,
            GeneralUserDTO createUser,
            FileContent geoTiffFile
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        ScheduleJobDTO job = new ScheduleJobDTO();
        job.setSystemId(systemId);
        job.setJobCreateUserId(createUser.getId());
        job.setJobCreateUserName(createUser.getUserName());
        job.setJobCreateTime(new Date());
        job.setJobType(SystemScheduleJobTypeEnum.IMPORT_GEOTIFF.getCode());
        job.setJobTaskCount(1L);
        // 保证继续任务时的进度和第一次下标为0时的处理
        job.setJobTaskOffset(-1L);
        job.setJobStatus(SystemScheduleJobStatusEnum.INIT.getCode());
        job.setJobMaxRetryTimes(3L);
        job.setJobCurrentRetryTimes(0L);
        Map<String, Object> jobParams = new HashMap<>();
        // 清空数据, 执行任务时重新获取原始数据流
        geoTiffFile.setBytes(new byte[0]);
        jobParams.put("systemId", systemId);
        jobParams.put("tableId", tableId);
        try {
            jobParams.put("createUser", objectMapper.writeValueAsString(createUser));
            jobParams.put("resourceFile", objectMapper.writeValueAsString(geoTiffFile));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        job.setJobParams(jobParams);
        return job;
    }
}
