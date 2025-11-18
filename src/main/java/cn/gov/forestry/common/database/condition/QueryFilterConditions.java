package cn.gov.forestry.common.database.condition;

import lombok.Data;

import java.util.List;

@Data
public class QueryFilterConditions {
    private String fieldName;
    private List<QueryCondition> conditions;
}
