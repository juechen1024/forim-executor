package cn.gov.forestry.common.database.crud;

import cn.gov.forestry.common.database.DatabaseInfo;
import lombok.Data;

@Data
public class DeleteByIdParams {
    private DatabaseInfo databaseInfo;
    private String tableEntityName;
    private String id;
}
