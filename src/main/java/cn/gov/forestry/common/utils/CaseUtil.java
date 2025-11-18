package cn.gov.forestry.common.utils;

import java.util.HashMap;
import java.util.Map;

public class CaseUtil {

    /**
     * 将 HashMap 的键从驼峰命名或大写字母转换为小写下划线格式。
     *
     * @param originalMap 原始 HashMap
     * @return 转换后的 HashMap
     */
    public static <K, V> HashMap<String, V> convertKeysToSnakeCase(Map<K, V> originalMap) {
        HashMap<String, V> newMap = new HashMap<>();

        for (Map.Entry<K, V> entry : originalMap.entrySet()) {
            String oldKey = entry.getKey().toString();
            V value = entry.getValue();

            // 转换键为小写下划线格式
            String newKey = toSnakeCase(oldKey);

            // 添加到新的 HashMap 中
            newMap.put(newKey, value);
        }

        return newMap;
    }

    /**
     * 将驼峰命名法转换为小写下划线格式（snake_case）。
     *
     * @param str 输入字符串
     * @return 转换后的字符串
     */
    public static String toSnakeCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 将下划线分隔的小写格式转换为驼峰命名法。
     *
     * @param str 输入字符串
     * @return 转换后的字符串
     */
    public static String toCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);

            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }

        return result.toString();
    }

    /**
     * 将字符串转换为全大写。
     *
     * @param str 输入字符串
     * @return 全大写的字符串
     */
    public static String toUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.toUpperCase();
    }

    /**
     * 将字符串转换为全小写。
     *
     * @param str 输入字符串
     * @return 全小写的字符串
     */
    public static String toLowerCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.toLowerCase();
    }
}
