package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import cn.gov.forestry.common.database.FieldValue;
import lombok.Data;

import java.util.Map;

@Data
public class InsertParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private Map<String, FieldValue> properties;
}
