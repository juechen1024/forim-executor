package cn.gov.forestry.common.rule;

import cn.gov.forestry.common.necromantic.bo.query.RangeQueryParamEnum;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

public enum QueryConditionOperatorEnum {
    // 等于
    EQ("10", "等于",""),
    // 大于
    GT("20","大于","$gt"),
    // 小于
    LT("30","小于","$lt"),
    // 大于等于
    GTE("21","大于等于","$gte"),
    // 小于等于
    LTE("31","小于等于","$lte"),
    // 不等于
    NE("40","不等于","$ne"),
    // 左右全模糊匹配
    LIKE_ALL("50","模糊匹配(*key*)","pattern"),
    // 模糊匹配左侧
    LIKE_LEFT("51","模糊匹配(key*)","pattern"),
    // 模糊匹配右侧
    LIKE_RIGHT("52","模糊匹配(*key)","pattern"),
    // 范围查询
    RANGE_START_END("60", "范围查询({fieldName}"+ RangeQueryParamEnum.START.getParamPostfix() +",{fieldName}" + RangeQueryParamEnum.END.getParamPostfix() + ")", "RangeQueryParamEnum")
    ;
    @Getter
    private String code;
    @Getter
    private String label;
    @Getter
    private String mongoOperator;
    QueryConditionOperatorEnum(String code, String label, String mongoOperator) {
        this.code = code;
        this.label = label;
        this.mongoOperator = mongoOperator;
    }
    /**
     * 根据code获取实例,如果为空就返回等于,默认等于
     * */
    public static QueryConditionOperatorEnum getInstanceByCode(String code) {
        if (ObjectUtils.isEmpty(code)) {
            return EQ;
        }
        for (QueryConditionOperatorEnum operator : QueryConditionOperatorEnum.values()) {
            if (operator.code.equals(code)) {
                return operator;
            }
        }
        return EQ;
    }

    public static Boolean isEq(QueryConditionOperatorEnum operator) {
        return EQ.equals(operator);
    }
    public static Boolean isEq(String code) {
        for (QueryConditionOperatorEnum operator : QueryConditionOperatorEnum.values()) {
            if (operator.getCode().equals(code)) {
                return isEq(operator);
            }
        }
        return Boolean.FALSE;
    }


    public static Boolean isLike(QueryConditionOperatorEnum operator) {
        return LIKE_ALL.equals(operator) || LIKE_LEFT.equals(operator) || LIKE_RIGHT.equals(operator);
    }
    public static Boolean isLike(String code) {
        for (QueryConditionOperatorEnum operator : QueryConditionOperatorEnum.values()) {
            if (operator.getCode().equals(code)) {
                return isLike(operator);
            }
        }
        return Boolean.FALSE;
    }
    public static Boolean isRangeStartEnd(String code) {
        return RANGE_START_END.getCode().equals(code);
    }

}
