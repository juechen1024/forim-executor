package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import lombok.Data;

@Data
public class ExistsByIdParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private String id;
}