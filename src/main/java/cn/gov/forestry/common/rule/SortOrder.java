package cn.gov.forestry.common.rule;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 排序方向枚举（支持多数据库扩展预留结构，当前主要支持 MongoDB）
 * 参照 QueryConditionsOperatorEnum 风格实现
 */
@Getter
public enum SortOrder {

    // 升序
    ASC("10", "升序", "ascend",1),

    // 降序
    DESC("20", "降序", "descend",-1);

    private final String code;
    private final String label;
    // ascend descend
    private final String antdSortString;
    private final Integer mongoOperator;

    SortOrder(String code, String label, String antdSortString, Integer mongoOperator) {
        this.code = code;
        this.label = label;
        this.antdSortString = antdSortString;
        this.mongoOperator = mongoOperator;
    }

    /**
     * 根据 code 获取对应的排序枚举实例
     * 如果 code 为空或无效，默认返回 ASC（升序）
     *
     * @param code 枚举编码
     * @return SortOrder 实例
     */
    public static SortOrder getInstanceByCode(String code) {
        if (ObjectUtils.isEmpty(code)) {
            return ASC;
        }
        for (SortOrder order : SortOrder.values()) {
            if (order.getCode().equals(code)) {
                return order;
            }
        }
        return ASC; // 默认升序
    }

    /**
     * 是否为升序
     */
    public static boolean isAsc(SortOrder order) {
        return ASC.equals(order);
    }

    /**
     * 是否为降序
     */
    public static boolean isDesc(SortOrder order) {
        return DESC.equals(order);
    }

    /**
     * 根据 code 判断是否为升序
     */
    public static boolean isAsc(String code) {
        return isAsc(getInstanceByCode(code));
    }

    /**
     * 根据 code 判断是否为降序
     */
    public static boolean isDesc(String code) {
        return isDesc(getInstanceByCode(code));
    }

    /**
     * 根据 antd 的排序字符串（ascend/descend）获取对应的 SortOrder 实例
     * 如果输入为空或不匹配，默认返回 ASC
     *
     * @param antdSortString antd 表格传来的排序值，如 "ascend", "descend"
     * @return SortOrder 实例
     */
    public static SortOrder getInstanceByAntdSortString(String antdSortString) {
        if (ObjectUtils.isEmpty(antdSortString)) {
            return ASC;
        }
        for (SortOrder order : SortOrder.values()) {
            if (order.getAntdSortString().equals(antdSortString)) {
                return order;
            }
        }
        return ASC; // 默认升序
    }

    /**
     * 工具方法：将 Ant Design 传入的 sort Map（字段 -> "ascend"/"descend"）
     * 转换为 字段 -> SortOrder.code（如 "10", "20"） 的 Map
     *
     * @param antdSortMap 前端传入的 sort 参数，例如 { "name": "ascend", "age": "descend" }
     * @return 转换后的 Map，key 不变，value 变为 SortOrder 的 code
     */
    public static Map<String, Object> convertAntdSortToCodeMap(Map<String, Object> antdSortMap) {
        Map<String, Object> result = new HashMap<>();
        if (antdSortMap == null || antdSortMap.isEmpty()) {
            return result;
        }

        for (Map.Entry<String, Object> entry : antdSortMap.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            String stringValue = value instanceof String ? (String) value : null;

            // 使用 getInstanceByAntdSortString 获取枚举，再取 code
            SortOrder sortOrder = SortOrder.getInstanceByAntdSortString(stringValue);
            result.put(field, sortOrder.getCode());
        }
        return result;
    }

}