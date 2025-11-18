package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemScheduleJobStatusEnum {
    INIT("0","初始化", "normal"),
    RUNNING("10","运行中", "active"),
    SUCCESS("100","完成", "success"),
    ERROR("500","失败", "exception"),
    ;
    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private String processName;

    SystemScheduleJobStatusEnum(String code, String name, String processName){
        this.code = code;
        this.name = name;
        this.processName = processName;
    }

    public static SystemScheduleJobStatusEnum getByCode(String code){
        for(SystemScheduleJobStatusEnum s : SystemScheduleJobStatusEnum.values()){
            if(s.getCode().equals(code)){
                return s;
            }
        }
        return null;
    }

    public static Boolean isRunning(String code){
        return RUNNING.getCode().equals(code);
    }
}
