package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.FieldValue;
import cn.gov.forestry.common.database.condition.QueryCondition;
import cn.gov.forestry.common.database.condition.QueryFilterConditions;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateByLmParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private List<QueryFilterConditions> lm;
    private Map<String, FieldValue> updateMap;
}
