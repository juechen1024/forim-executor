package cn.gov.forestry.common.rule;

import lombok.Data;

import java.util.List;

@Data
public class DataFilterRule {

    // 当前用户属性名
    private String userProperty;

    // 数据库字段
    private String fieldName;

    // 匹配类型：等于、包含、正则匹配等
    /**
     * @see QueryConditionOperatorEnum
     * */
    private String operatorCode;

    // 可选：额外的值（如白名单、正则表达式）
    private List<String> values;
}
