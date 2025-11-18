package cn.gov.forestry.common.database;

import cn.gov.forestry.common.domain.bo.SystemFieldDataTypeEnum;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * FieldValue 静态构建工具类
 * 提供静态工厂方法，用于创建不同数据类型的 FieldValue 实例
 */
@NoArgsConstructor(staticName = "getInstance") // 禁止实例化
public class FieldValueBuilder {

    /**
     * 创建 STRING 类型的 FieldValue
     */
    public static FieldValue string(String value) {
        return createFieldValue(SystemFieldDataTypeEnum.STRING, value);
    }

    /**
     * 创建 INTEGER 类型的 FieldValue
     */
    public static FieldValue integer(Integer value) {
        return createFieldValue(SystemFieldDataTypeEnum.INTEGER, value);
    }
    /**
     * 创建 INTEGER 类型的 FieldValue
     */
    public static FieldValue integer(Long value) {
        return createFieldValue(SystemFieldDataTypeEnum.INTEGER, value);
    }

    /**
     * 创建 DOUBLE 类型的 FieldValue
     */
    public static FieldValue doubleValue(Double value) {
        return createFieldValue(SystemFieldDataTypeEnum.DOUBLE, value);
    }

    /**
     * 创建 BOOLEAN 类型的 FieldValue
     */
    public static FieldValue booleanValue(Boolean value) {
        return createFieldValue(SystemFieldDataTypeEnum.BOOLEAN, value);
    }

    /**
     * 创建 DATE 类型的 FieldValue
     * value 可以是 Date, Instant, String 等
     */
    public static FieldValue date(Object value) {
        return createFieldValue(SystemFieldDataTypeEnum.DATE, value);
    }

    /**
     * 创建 OBJECT 类型的 FieldValue
     * value 通常是 Map、Document 或 POJO
     */
    public static FieldValue object(Object value) {
        return createFieldValue(SystemFieldDataTypeEnum.OBJECT, value);
    }

    /**
     * 创建 ARRAY 类型的 FieldValue
     * value 通常是 List、Object[] 等
     */
    public static FieldValue array(Object value) {
        return createFieldValue(SystemFieldDataTypeEnum.ARRAY, value);
    }

    // 创建 FieldValue 并校验 value
    public static FieldValue createFieldValue(SystemFieldDataTypeEnum type, Object value) {
        FieldValue fieldValue = new FieldValue();
        fieldValue.setDataTypeCode(type.getCode());
        fieldValue.setValue(value);
        return fieldValue;
    }

    public static Map<String, FieldValue> convertObjectMap(Map<String, Object> objectMap) {
        Map<String, FieldValue> result = new HashMap<>(objectMap.size());
        // 遍历并转换每个 value
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 使用静态工厂方法 object() 构建 FieldValue
            FieldValue fieldValue = FieldValueBuilder.object(value);

            // 放入新 map
            result.put(key, fieldValue);
        }
        return result;
    }
}