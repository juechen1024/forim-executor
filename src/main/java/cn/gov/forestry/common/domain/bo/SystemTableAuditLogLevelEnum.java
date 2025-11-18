package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemTableAuditLogLevelEnum {
    IGNORE("0", "不记录", 0),
    MODIFY("10", "增删改", 10),
    FULL("20", "增删改查", 20),
    ;
    @Getter
    private String code;
    @Getter
    private String label;
    @Getter
    private Integer level;

    SystemTableAuditLogLevelEnum(String code, String label, Integer level) {
        this.code = code;
        this.label = label;
        this.level = level;
    }

    public static Boolean isIgnoreAuditLog(Integer level) {
        return IGNORE.getLevel().equals(level);
    }
}
