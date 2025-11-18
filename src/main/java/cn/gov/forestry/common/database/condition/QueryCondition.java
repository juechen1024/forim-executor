package cn.gov.forestry.common.database.condition;

import cn.gov.forestry.common.database.FieldValue;
import cn.gov.forestry.common.rule.QueryConditionOperatorEnum;
import lombok.Data;

@Data
public class QueryCondition {
    // eq gt lt
    private QueryConditionOperatorEnum operator;

    private FieldValue fieldValue;

    public QueryCondition() {
    }

    public QueryCondition(QueryConditionOperatorEnum operator, FieldValue fieldValue) {
        this.operator = operator;
        this.fieldValue = fieldValue;
    }
    public QueryCondition(String operatorCode, FieldValue fieldValue) {
        this.operator = QueryConditionOperatorEnum.getInstanceByCode(operatorCode);
        this.fieldValue = fieldValue;
    }
}
