package cn.gov.forestry.common.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UniversalIdUtil {

    private static final long NULL_ID = -1L; // 可自定义 null 的映射值
    private static final long FALSE_ID = 0L;
    private static final long TRUE_ID = 1L;

    /**
     * 将任意对象转换为一个尽可能唯一的 long ID
     * @param obj 输入对象（支持 String, Number, UUID, Boolean, null 等）
     * @return 唯一性较强的 long 值
     */
    public static long toUniqueId(Object obj) {
        if (obj == null) {
            return NULL_ID;
        }

        // 1. 如果已经是 Long，直接返回（避免哈希开销）
        if (obj instanceof Long l) {
            return l;
        }

        // 2. 数值类型：统一通过 Number 提取 longValue
        if (obj instanceof Number num) {
            return num.longValue(); // 自动处理 Integer, Double, Float 等
        }

        // 3. 布尔类型
        if (obj instanceof Boolean b) {
            return b ? TRUE_ID : FALSE_ID;
        }

        // 4. UUID 类型：使用其内部两个 long 的异或（或直接哈希）
        if (obj instanceof UUID uuid) {
            return (uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits());
        }

        // 5. 字符串类型（包括 ObjectId, UUID 字符串等）
        if (obj instanceof String str) {
            return hashToLong(str);
        }

        // 6. 其他任意对象：使用其 toString() + 哈希
        // 注意：确保 toString() 是稳定且唯一的（如 JPA Entity 可能需重写）
        return hashToLong(obj.toString());
    }

    /**
     * 使用 MurmurHash3-128 截取为 64 位 long
     * 高均匀性、低碰撞、高性能
     */
    private static long hashToLong(String str) {
        if (str.isEmpty()) {
            return 0L;
        }
        return Hashing.murmur3_128()
                .hashString(str, StandardCharsets.UTF_8)
                .asLong();
    }

    // --- 可选：重载方法方便调用 ---
    public static long fromString(String str) {
        return str == null ? NULL_ID : hashToLong(str);
    }

    public static long fromNumber(Number num) {
        return num == null ? NULL_ID : num.longValue();
    }
}