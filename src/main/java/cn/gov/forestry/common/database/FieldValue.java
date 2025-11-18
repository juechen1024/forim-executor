package cn.gov.forestry.common.database;

import lombok.Data;

@Data
public class FieldValue {
    /**
     * @see cn.gov.forestry.common.domain.bo.SystemFieldDataTypeEnum
     * */
    private String dataTypeCode;
    private Object value;
}
