package cn.gov.forestry.common.utils;

public class NormalizeUtil {
    /**
     * 规范化 S3 Prefix：
     * - 去除前导 /
     * - 合并中间多个 / 为单个 /
     * - 可选择是否保留末尾 /
     * - 防空处理
     */
    public static String normalizeS3Prefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return "";
        }

        // 去除前导和尾部空白
        String cleaned = prefix.trim();

        // 替换所有连续的 / 为单个 /
        cleaned = cleaned.replaceAll("/+", "/");

        // 去除前导 /
        if (cleaned.startsWith("/")) {
            cleaned = cleaned.substring(1);
        }

        // 可选：保留末尾 /（如果你希望表示“目录”）
        // 如果不希望保留，取消下面这行
        // if (!cleaned.isEmpty() && !cleaned.endsWith("/") && !cleaned.contains(".")) {
        //     cleaned += "/";
        // }

        return cleaned;
    }
}
