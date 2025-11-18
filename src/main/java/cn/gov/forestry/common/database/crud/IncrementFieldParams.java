package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import lombok.Data;

@Data
public class IncrementFieldParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private String id;
    private String field;
    private Long increment = 1L; // 默认 +1
}