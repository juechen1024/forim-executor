package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.FieldValue;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InsertBatchParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private List<Map<String, FieldValue>> propertiesList;
}
