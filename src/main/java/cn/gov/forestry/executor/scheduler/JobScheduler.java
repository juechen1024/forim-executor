package cn.gov.forestry.executor.scheduler;

import cn.gov.forestry.common.domain.bo.SystemScheduleJobStatusEnum;
import cn.gov.forestry.common.domain.dto.schedule.ScheduleJobDTO;
import cn.gov.forestry.executor.client.ScheduleInnerJobClient;
import cn.gov.forestry.executor.job.ScheduleJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Slf4j
public class JobScheduler {
    private final ScheduleInnerJobClient scheduleInnerJobClient;
    private final ScheduleJobExecutor scheduleJobExecutor;

    public JobScheduler(ScheduleInnerJobClient scheduleInnerJobClient, ScheduleJobExecutor scheduleJobExecutor) {
        this.scheduleInnerJobClient = scheduleInnerJobClient;
        this.scheduleJobExecutor = scheduleJobExecutor;
    }

    //调试时间一分钟测试一次
    @Scheduled(cron="0 */1 * * * ?")
    // 每小时执行一次
    //@Scheduled(cron = "0 0 */1 * * ?")
    public void checkJobStatus() {
        LOGGER.info("JobScheduler-start-cron-checking-job-status.....");
        // 获取正在运行的任务
        ScheduleJobDTO runningJobParam = new ScheduleJobDTO();
        runningJobParam.setJobStatus(SystemScheduleJobStatusEnum.RUNNING.getCode());
        List<ScheduleJobDTO> runningJobs = scheduleInnerJobClient.getScheduleJobs(runningJobParam);
        if (ObjectUtils.isEmpty(runningJobs)) {
            LOGGER.info("JobScheduler-no-running-jobs-start-checking-init-jobs");
            ScheduleJobDTO initJobParam = new ScheduleJobDTO();
            initJobParam.setJobStatus(SystemScheduleJobStatusEnum.INIT.getCode());
            List<ScheduleJobDTO> initJobs = scheduleInnerJobClient.getScheduleJobs(initJobParam);
            if (!ObjectUtils.isEmpty(initJobs)) {
                scheduleJobExecutor.execute(initJobs.get(0));
            } else {
                LOGGER.info("JobScheduler-no-init-jobs");
            }
        } else {
            LOGGER.info("JobScheduler-running-jobs-count-[{}]", runningJobs.size());
        }
        LOGGER.info("JobScheduler-end-cron");
    }
}
