package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum RedisSeriesKeyEnum {
    //资源-用户-自增键
    GENERAL_SYSTEM_ID("series:general:system:id"),
    GENERAL_SYSTEM_ORDER("series:general:system:order"),

    //资源-用户-自增键
    GENERAL_USER_ID("series:general:user:id"),

    //资源-用户-排序值
    GENERAL_USER_ORDER("series:general:user:order"),

    //资源-角色-自增键
    GENERAL_ROLE_ID("series:general:role:id"),

    //资源-角色-排序值
    GENERAL_ROLE_ORDER("series:general:role:order"),

    //资源-资源功能-自增键
    GENERAL_RESOURCE_ID("series:general:resource:id"),

    //资源-资源功能-排序值
    GENERAL_RESOURCE_ORDER("series:general:resource:order"),

    // 系统-源信息-唯一自增键
    SYSTEM_METADATA_TABLE_ORDER("series:system:metadata:table:order"),

    SYSTEM_METADATA_FIELD_ORDER("series:system:metadata:field:order"),

    SYSTEM_METADATA_ENUM_ORDER("series:system:metadata:enum:order"),
    SYSTEM_METADATA_CODE_ORDER("series:system:metadata:code:order"),
    // 系统-异步任务-排序
    SYSTEM_SCHEDULE_JOB_ORDER("series:system:schedule:job:order"),

    // pruducts-表记录排序
    PRODUCTS_TABLE_RECORD_ORDER_PREFIX("series:products:"),
    PRODUCTS_TABLE_RECORD_ORDER_POSTFIX(":record:order"),
    ;

    @Getter
    private String key;

    RedisSeriesKeyEnum(String key){
        this.key = key;
    }

    public static String getProductsTableRecordOrderKey(String systemId, String tableId, String orderField) {
        return PRODUCTS_TABLE_RECORD_ORDER_PREFIX + systemId + ":" + tableId + ":" + orderField + PRODUCTS_TABLE_RECORD_ORDER_POSTFIX;
    }
}
