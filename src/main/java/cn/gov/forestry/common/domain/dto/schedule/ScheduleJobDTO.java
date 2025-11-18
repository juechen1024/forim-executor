package cn.gov.forestry.common.domain.dto.schedule;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ScheduleJobDTO {
    private String systemId;

    private String id;

    private String jobCreateUserId;
    private String jobCreateUserName;

    private Date jobCreateTime;

    // SystemScheduleJobTypeEnum
    private String jobType;

    private Long jobTaskCount;

    private Long jobTaskOffset;

    // SystemScheduleJobStatusEnum
    private String jobStatus;

    private Long jobMaxRetryTimes;

    private Long jobCurrentRetryTimes;

    private Map<String, Object> jobParams;

    private Map<String, Object> jobResult;

    private Long jobOrder;

    private Date jobStartTime;

    private Date jobEndTime;

    private Map<String, Object> additionalProperties;
}
