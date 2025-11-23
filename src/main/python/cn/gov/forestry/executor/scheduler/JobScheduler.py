import logging
from typing import List

from cn.gov.forestry.common.domain.bo.system_schedule_job_status_enum import SystemScheduleJobStatusEnum
from cn.gov.forestry.common.domain.dto.schedule.schedule_job_dto import ScheduleJobDTO
from cn.gov.forestry.executor.client.ScheduleInnerJobClient import ScheduleInnerJobClient
from cn.gov.forestry.executor.job.ScheduleJobExecutor import ScheduleJobExecutor

logger = logging.getLogger(__name__)


class JobScheduler:
    def __init__(self, scheduleInnerJobClient: ScheduleInnerJobClient, scheduleJobExecutor: ScheduleJobExecutor):
        self.scheduleInnerJobClient = scheduleInnerJobClient
        self.scheduleJobExecutor = scheduleJobExecutor

    def checkJobStatus(self) -> None:
        logger.info("JobScheduler-start-cron-checking-job-status.....")
        runningJobParam = ScheduleJobDTO()
        runningJobParam.jobStatus = SystemScheduleJobStatusEnum.RUNNING.code
        runningJobs: List[ScheduleJobDTO] = self.scheduleInnerJobClient.getScheduleJobs(runningJobParam)
        if not runningJobs:
            logger.info("JobScheduler-no-running-jobs-start-checking-init-jobs")
            initJobParam = ScheduleJobDTO()
            initJobParam.jobStatus = SystemScheduleJobStatusEnum.INIT.code
            initJobs = self.scheduleInnerJobClient.getScheduleJobs(initJobParam)
            if initJobs:
                self.scheduleJobExecutor.execute(initJobs[0])
            else:
                logger.info("JobScheduler-no-init-jobs")
        else:
            logger.info("JobScheduler-running-jobs-count-[%s]", len(runningJobs))
        logger.info("JobScheduler-end-cron")


__all__ = ["JobScheduler"]
