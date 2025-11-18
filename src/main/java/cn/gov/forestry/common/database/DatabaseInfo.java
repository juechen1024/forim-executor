package cn.gov.forestry.common.database;

import cn.gov.forestry.common.domain.bo.SystemDatabaseTypeEnum;
import cn.gov.forestry.common.domain.dto.general.GeneralSystemDTO;
import lombok.Data;

@Data
public class DatabaseInfo {
    private String systemId;
    private String databaseType;
    private String databaseHost;
    private String databasePort;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;

    public static DatabaseInfo fromSystemInfo(GeneralSystemDTO generalSystemDTO) {
        DatabaseInfo result = new DatabaseInfo();
        result.setSystemId(generalSystemDTO.getId());
        result.setDatabaseType(generalSystemDTO.getSystemDatabaseType());
        result.setDatabaseHost(generalSystemDTO.getSystemDatabaseHost());
        result.setDatabasePort(generalSystemDTO.getSystemDatabasePort());
        result.setDatabaseUsername(generalSystemDTO.getSystemDatabaseUsername());
        result.setDatabasePassword(generalSystemDTO.getSystemDatabasePassword());
        result.setDatabaseName(generalSystemDTO.getSystemDatabaseName());
        return result;
    }

    public String getConnectString() {
        if (SystemDatabaseTypeEnum.isMongo(this.getDatabaseType())) {
            return "mongodb://"+
                    this.getDatabaseUsername()+
                    ":"+
                    this.getDatabasePassword()+
                    "@"+
                    this.getDatabaseHost()+
                    ":"+
                    this.getDatabasePort()+
                    "/?authSource="+
                    this.getDatabaseName();
        } else {
            return "";
        }
    }
}
