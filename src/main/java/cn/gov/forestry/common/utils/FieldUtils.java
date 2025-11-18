package cn.gov.forestry.common.utils;

import cn.gov.forestry.common.database.FieldValue;
import cn.gov.forestry.common.domain.bo.SystemFieldDataTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class FieldUtils {
    /**
     * 根据字段类型枚举，尝试将值转换为对应类型
     *
     * @param value       原始值
     * @param fieldType   字段类型枚举
     * @return 转换后的值，失败时返回原始值
     */
    public static Object convertValueByFieldType(Object value, SystemFieldDataTypeEnum fieldType) {
        // null 值直接返回 null（也可返回原始 null）
        if (value == null) {
            return null;
        }

        try {
            switch (fieldType) {
                case INTEGER:
                    // 实际代表整数，用long类型
                    if (value instanceof Number) {
                        return ((Number) value).longValue();
                    } else if (value instanceof String && StringUtils.hasText((String) value)) {
                        return Long.parseLong((String) value);
                    }
                    break;

                case DOUBLE:
                    if (value instanceof Number) {
                        return ((Number) value).doubleValue();
                    } else if (value instanceof String && StringUtils.hasText((String) value)) {
                        return Double.parseDouble((String) value);
                    }
                    break;

                case BOOLEAN:
                    if (value instanceof Boolean) {
                        return value;
                    } else if (value instanceof String) {
                        String str = ((String) value).trim().toLowerCase();
                        if ("true".equals(str) || "1".equals(str) || "on".equals(str)) {
                            return true;
                        } else if ("false".equals(str) || "0".equals(str) || "off".equals(str)) {
                            return false;
                        }
                    }
                    break;

                case STRING:
                    return value.toString();

                case DATE:
                    // 会尝试将任意格式转为instant
                    return DateUtil.toInstant(value);

                case OBJECT:
                    //if (value instanceof Map) {
                    //    return value;
                    //}
                    // 直接返回
                    return value;
                    //break;

                case ARRAY:
                    //if (value instanceof List) {
                    //    return value;
                    //}
                    // 直接返回
                    return value;
                    //break;
            }
        } catch (Exception e) {
            // 转换失败，返回原始值
            LOGGER.error("FieldUtils-convertValueByFieldType-try-to-convert-error-value:[{}]-dataType-[{}]", value, fieldType.getCode(), e);
            return value;
        }

        // 默认：无法转换，返回原始值
        return value;
    }

    /**
     * 根据FieldValue.dataTypeCode，尝试将值提取为对应类型
     *
     * @param fieldValue       FieldValue
     * @return 提取后的值，失败时返回原始值
     */
    public static Object extractValue(FieldValue fieldValue) {
        return convertValueByFieldType(fieldValue.getValue(), SystemFieldDataTypeEnum.getByCodeOrDefault(fieldValue.getDataTypeCode()));
    }
}
