package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import lombok.Data;

import java.util.Map;

@Data
public class SelectAllParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private Map<String, Object> projection;
}