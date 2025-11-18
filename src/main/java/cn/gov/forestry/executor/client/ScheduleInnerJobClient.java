package cn.gov.forestry.executor.client;

import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobDTO;
import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobLogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "scheduleInnerJobClient", url = "${forim.inner.schedule.url}")
public interface ScheduleInnerJobClient {
    @PostMapping(value = "/inner/schedule/create/asynchronous/job")
    ScheduleJobDTO createAsynchronousJob(@RequestBody ScheduleJobDTO dto);

    @PostMapping(value = "/inner/schedule/update/asynchronous/job")
    Long updateAsynchronousJob(@RequestBody ScheduleJobDTO dto);

    @PostMapping(value = "/inner/schedule/get/asynchronous/job/list")
    List<ScheduleJobDTO> getScheduleJobs(@RequestBody ScheduleJobDTO dto);

    @PostMapping(value = "/inner/schedule/create/asynchronous/job/log")
    ScheduleJobLogDTO createScheduleJobLog(@RequestBody ScheduleJobLogDTO dto);
}
