package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.condition.QueryCondition;
import cn.gov.forestry.common.database.condition.QueryFilterConditions;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DistinctFieldByLmParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private String field; // 要去重的字段名
    private List<QueryFilterConditions> lm;
}