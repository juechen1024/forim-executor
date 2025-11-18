package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.condition.QueryFilterConditions;
import lombok.Data;

import java.util.List;

@Data
public class DeleteByLmParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private List<QueryFilterConditions> lm;
}