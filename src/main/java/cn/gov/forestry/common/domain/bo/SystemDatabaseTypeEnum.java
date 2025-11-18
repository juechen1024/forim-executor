package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

public enum SystemDatabaseTypeEnum {
    MONGODB("110", "MongoDB", "NoSQL"),
    CASSANDRA("120", "Cassandra", "NoSQL"),
    HBASE("130", "HBase", "NoSQL"),
    REDIS("140", "Redis", "NoSQL"),
    ELASTICSEARCH("150", "Elasticsearch", "NoSQL"),
    POSTGRESQL("210", "PostgreSQL", "RDBMS"),
    MYSQL("220", "MySQL", "RDBMS"),
    ORACLE("230", "Oracle", "RDBMS"),
    SQLSERVER("240", "SQLServer", "RDBMS");

    @Getter
    private final String code;

    @Getter
    private final String name;

    @Getter
    private final String databaseCategory; // 新增字段：数据库类别（"RDBMS" 或 "NoSQL"）

    SystemDatabaseTypeEnum(String code, String name, String databaseCategory) {
        this.code = code;
        this.name = name;
        this.databaseCategory = databaseCategory;
    }

    /**
     * 根据 code 获取对应的枚举值
     *
     * @param code 数据库类型编码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static SystemDatabaseTypeEnum fromCode(String code) {
        for (SystemDatabaseTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据 name 获取对应的枚举值
     *
     * @param name 数据库类型名称
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static SystemDatabaseTypeEnum fromName(String name) {
        for (SystemDatabaseTypeEnum type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 检查给定的数据库类型是否为关系型数据库
     *
     * @param code 数据库类型的编码
     * @return 如果是关系型数据库返回 true，否则返回 false
     */
    public static boolean isRdbmsByCode(String code) {
        SystemDatabaseTypeEnum type = fromCode(code);
        return type != null && "RDBMS".equalsIgnoreCase(type.getDatabaseCategory());
    }

    /**
     * 检查给定的数据库类型是否为关系型数据库
     *
     * @param name 数据库类型的名称
     * @return 如果是关系型数据库返回 true，否则返回 false
     */
    public static boolean isRdbmsByName(String name) {
        SystemDatabaseTypeEnum type = fromName(name);
        return type != null && "RDBMS".equalsIgnoreCase(type.getDatabaseCategory());
    }

    public static boolean isMongo(String code) {
        return MONGODB.getCode().equals(code);
    }
}