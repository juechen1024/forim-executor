package cn.gov.forestry.common.utils;
import net.sourceforge.pinyin4j.PinyinHelper;
import java.util.*;
public class ChineseUtils {

    /**
     * 将 map 的 key 转换为拼音，中文转英文，非中文保留原 key
     * 拼音重复时自动加 _1, _2 等后缀
     */
    public static Map<String, Object> convertKeysToPinyinWithUniqueSuffix(Map<String, Object> originalMap) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Integer> pinyinCounter = new HashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            String pinyinKey = containsChinese(key) ? toPinyin(key) : key;

            // 处理重复拼音
            if (pinyinCounter.containsKey(pinyinKey)) {
                int count = pinyinCounter.get(pinyinKey) + 1;
                pinyinCounter.put(pinyinKey, count);
                pinyinKey = pinyinKey + "_" + count;
            } else {
                pinyinCounter.put(pinyinKey, 1);
            }

            result.put(pinyinKey, value);
        }

        return result;
    }

    /**
     * 判断字符串是否全为中文字符
     */
    private static boolean isChineseString(String str) {
        return str != null && str.matches("[\\u4E00-\\u9FA5]+");
    }
    /**
     * 判断字符串是否包含中文字符
     */
    public static boolean containsChinese(String str) {
        if (str == null) return false;
        return str.matches(".*[\\u4E00-\\u9FA5].*");
    }

    /**
     * 汉字转拼音，只取第一个发音，小写，无声调
     */
    private static String toPinyin(String chinese) {
        StringBuilder sb = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyinArray != null) {
                // 只取第一个发音，可优化为多音字选择器
                sb.append(pinyinArray[0].replaceAll("\\d", ""));
            } else {
                sb.append(c); // 非汉字保留原字符
            }
        }
        return sb.toString().toLowerCase();
    }
}
