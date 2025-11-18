package cn.gov.forestry.common.domain.dto.schedule;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ScheduleJobLogDTO {
    private String systemId;

    private String jobId;

    // SystemScheduleJobTypeEnum
    private String jobType;

    private String jobExecutor;

    private Integer jobLogLevel;

    private Date jobLogTime;

    private String jobLogContent;

    private Date createTime;

    private Map<String, Object> additionalProperties;
}
