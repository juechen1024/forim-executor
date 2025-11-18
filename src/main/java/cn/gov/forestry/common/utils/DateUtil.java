package cn.gov.forestry.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class DateUtil {
    public final static ZoneId GMT_8_ZONE = ZoneId.of("GMT+8");
    // 常见的 时间格式（支持 / 和 -，支持非补零）
    private static final List<DateTimeFormatter> COMMON_FORMATTERS = List.of(
            // ISO 标准
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,                    // 2025-08-29T11:30:00
            DateTimeFormatter.ISO_LOCAL_DATE,                        // 2025-08-29

            // 常规格式（支持 / 和 -）
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),      // 2024-01-24 12:30:45
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),         // 2024-01-24 12:30
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH"),            // 2024-01-24 12
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),               // 2024-01-24

            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),      // 2024/1/24 0:00:00
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),         // 2024/1/24 0:00
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH"),            // 2024/1/24 0
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),               // 2024/1/24

            // 单数字月/日补空格（少见但存在）
            DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"),          // 24/1/24 0:00
            DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"),           // 24-01-24 12:30

            // 中文格式（某些导出）
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd日"),

            // 仅时间（少见，但可配合 today）
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm")
    );

    /**
     * 格式常量
     */
    public final static String FMT_DATE = "yyyy-MM-dd";
    public final static String FMT_DATE_YMD_DESC = "yyyy年MM月dd日";
    public final static String FMT_TIME = "HH:mm:ss";
    public final static String FMT_TIME_HH_MM = "HH:mm";
    public final static String FMT_DATE_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String FMT_DATE_HH_MM_DESC = "yyyyMMddHHmm";
    public final static String FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FMT_DATE_TIME_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String FMT_DATE_HH_MM_CON = "yyyy-MM-dd-HH-mm";
    /**
     * 前端antd-dayjs专用的时间格式器：UTC、带毫秒、'Z' 后缀
     * 格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    private static final DateTimeFormatter ISO_8601_FOR_FRONTEND =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneOffset.UTC);

    // 预定义格式化器（线程安全）
    private static final DateTimeFormatter FORMATTER_DATE_TIME = DateTimeFormatter.ofPattern(FMT_DATE_TIME);
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(FMT_DATE);
    private static final DateTimeFormatter FORMATTER_TIME_HH_MM = DateTimeFormatter.ofPattern(FMT_TIME_HH_MM);
    private static final DateTimeFormatter FORMATTER_ISO_UTC = DateTimeFormatter.ofPattern(FMT_DATE_TIME_UTC).withZone(ZoneOffset.UTC);

    /**
     * 计算两个时间之间的天数差（绝对值）
     */
    public static long daysBetween(long startTimeMillis, long endTimeMillis) {
        Instant start = Instant.ofEpochMilli(startTimeMillis);
        Instant end = Instant.ofEpochMilli(endTimeMillis);
        return Duration.between(start, end).abs().toDays();
    }

    /**
     * 计算当前时间与指定时间之间的天数差
     */
    public static long daysBetween(long endTimeMillis) {
        Instant end = Instant.ofEpochMilli(endTimeMillis);
        Instant now = Instant.now();
        return Duration.between(end, now).abs().toDays();
    }

    /**
     * 计算当前时间与 (endTime + milli) 之间的分钟差（绝对值）
     */
    public static long minutesBetween(long endTimeMillis, long milli) {
        Instant target = Instant.ofEpochMilli(endTimeMillis + milli);
        Instant now = Instant.now();
        return Duration.between(target, now).abs().toMinutes();
    }

    /**
     * 计算两个时间之间相差的“向上取整天数”（用于分页、统计等）
     * 例如：相差 1 天零 1 秒，返回 2
     */
    public static long millisBetween4round(long startTimeMillis, long endTimeMillis) {
        Instant start = Instant.ofEpochMilli(startTimeMillis);
        Instant end = Instant.ofEpochMilli(endTimeMillis);
        Duration duration = Duration.between(start, end).abs();

        long days = duration.toDays();
        long remainingMillis = duration.toMillis() % (24 * 3600 * 1000);

        return days + (remainingMillis > 0 ? 1 : 0);
    }

    /**
     * 计算两个时间之间的小时差（绝对值）
     */
    public static long hourBetween(long startTimeMillis, long endTimeMillis) {
        Instant start = Instant.ofEpochMilli(startTimeMillis);
        Instant end = Instant.ofEpochMilli(endTimeMillis);
        return Duration.between(start, end).abs().toHours();
    }

    /**
     * 获取当前时间的格式化字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String getStringDate() {
        return Instant.now().atZone(GMT_8_ZONE)
                .format(FORMATTER_DATE_TIME);
    }

    /**
     * 将毫秒时间戳转为格式化字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String getStringDate(long milliSec) {
        if (milliSec == 0) return "";
        return Instant.ofEpochMilli(milliSec)
                .atZone(GMT_8_ZONE)
                .format(FORMATTER_DATE_TIME);
    }

    /**
     * 将毫秒时间戳转为指定格式的字符串
     */
    public static String getStringFmtDate(long milliSec, String format) {
        if (milliSec == 0) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return Instant.ofEpochMilli(milliSec)
                .atZone(GMT_8_ZONE)
                .format(formatter);
    }

    /**
     * 将格式化时间字符串转为毫秒时间戳
     * 支持格式：yyyy-MM-dd HH:mm:ss
     */
    public static Long getStringDate(String dateTime) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(dateTime, FORMATTER_DATE_TIME);
            return ldt.atZone(GMT_8_ZONE).toInstant().toEpochMilli();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 获取 Instant 对应的日期部分字符串（yyyy-MM-dd）
     */
    public static String getStringDate(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(GMT_8_ZONE)
                .format(FORMATTER_DATE);
    }

    /**
     * 获取 Instant 对应的日期时间字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String getStringDateTime(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(GMT_8_ZONE)
                .format(FORMATTER_DATE_TIME);
    }

    /**
     * 获取 Instant 对应的 HH:mm 字符串
     */
    public static String getStringDateHHMM(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(GMT_8_ZONE)
                .format(FORMATTER_TIME_HH_MM);
    }

    /**
     * 获取两个时间之间的天数间隔（向上取整）
     * 如果有剩余时间（哪怕1毫秒），就 +1 天
     */
    public static int getIntervalDays(Long startMillis, Long endMillis) {
        if (startMillis == null || endMillis == null) return -1;
        Instant start = Instant.ofEpochMilli(startMillis);
        Instant end = Instant.ofEpochMilli(endMillis);
        Duration duration = Duration.between(start, end).abs();
        long days = duration.toDays();
        long remainingMillis = duration.toMillis() % (24 * 3600 * 1000);
        return (int) (days + (remainingMillis > 0 ? 1 : 0));
    }

    /**
     * 将字符串解析为 Instant（支持多种格式）
     */
    public static Instant getDateByStr(String dateTimeStr, String format) {
        if (dateTimeStr == null || format == null) return null;
        try {
            if ("yyyy-MM-dd".equals(format)) {
                LocalDate date = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern(format));
                return date.atStartOfDay(GMT_8_ZONE).toInstant();
            } else {
                LocalDateTime ldt = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(format));
                return ldt.atZone(GMT_8_ZONE).toInstant();
            }
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 解析标准格式 yyyy-MM-dd HH:mm:ss 的字符串为 Instant
     */
    public static Instant getDateTimeByStr(String dateTimeStr) {
        return getDateByStr(dateTimeStr, FMT_DATE_TIME);
    }

    /**
     * 格式化 Instant 为指定格式的字符串
     */
    public static String getStringDateByInstantAndFormat(Instant instant, String format) {
        if (instant == null || format == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return instant.atZone(GMT_8_ZONE).format(formatter);
    }

    /**
     * 获取当前时间
     *
     * @param type 1: 毫秒时间戳, 2: 秒时间戳
     * @return 时间戳
     */
    public static Long getNowTime(int type) {
        Instant now = Instant.now();
        return type == 1 ? now.toEpochMilli() : now.getEpochSecond();
    }

    /**
     * 获取当前时间的 UTC 格式字符串（yyyy-MM-dd'T'HH:mm:ss'Z'）
     */
    public static String getAliTimestamp() {
        return Instant.now().atZone(ZoneOffset.UTC).format(FORMATTER_ISO_UTC);
    }

    /**
     * 获取当前时间按指定格式的字符串
     */
    public static String getDateByFormat(String pattern) {
        if (pattern == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return Instant.now().atZone(GMT_8_ZONE).format(formatter);
    }

    /**
     * 判断字符串是否为有效的日期时间格式（ISO 支持）
     */
    public static boolean isDateTimeString(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            // 尝试解析为 Instant
            Instant.parse(str);
            return true;
        } catch (DateTimeParseException e) {
            try {
                // 尝试标准格式
                LocalDateTime.parse(str, FORMATTER_DATE_TIME);
                return true;
            } catch (DateTimeParseException ex) {
                return false;
            }
        }
    }

    /**
     * 解析字符串为 LocalDateTime（主要用于展示，非存储）
     */
    public static LocalDateTime parseDateTime(String str) {
        try {
            return LocalDateTime.parse(str, FORMATTER_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }
    /**
     * 判断对象是否为可表示 Instant 的值
     * 满足以下任一条件返回 true：
     * 1. 对象是 java.time.Instant 实例
     * 2. 对象是字符串，且为合法的 ISO 8601 UTC 格式（如 "2025-08-29T16:30:45.123Z"）
     *
     * @param obj 待判断的对象
     * @return 是否为 Instant 或合法的 ISO 8601 UTC 字符串
     */
    public static boolean isInstantValue(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Instant) {
            return true;
        }

        if (obj instanceof String) {
            String str = ((String) obj).trim();
            if (str.isEmpty()) return false;
            try {
                Instant.parse(str);
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        return false;
    }
    /**
     * 判断对象是否为时间类型或可解析为时间的字符串/数值
     * 支持：Instant, LocalDateTime, ZonedDateTime, OffsetDateTime,
     *       Date, Calendar, Long (10/13位时间戳), String (标准时间格式或时间戳)
     *
     * @param obj 待判断对象
     * @return 是否为时间类型
     */
    public static boolean isTimeObject(Object obj) {
        return toInstant(obj) != null;
    }

    /**
     * 将任意对象尝试转换为 Instant 时间点
     * 支持类型：
     * - Instant
     * - LocalDateTime, ZonedDateTime, OffsetDateTime
     * - java.util.Date, java.util.Calendar
     * - Long (10位秒级或13位毫秒级时间戳)
     * - String: ISO, 标准格式, yyyy/MM/dd HH:mm, 纯数字等
     *
     * @param obj 输入对象
     * @return 解析成功的 Instant
     * @throws IllegalArgumentException 解析失败时抛出
     */
    public static Instant toInstant(Object obj) {
        if (obj == null) {
            return null;
        }

        // 1. 已经是 Instant
        if (obj instanceof Instant instant) {
            return instant;
        }

        // 2. LocalDateTime -> Instant（使用系统默认时区）
        if (obj instanceof LocalDateTime ldt) {
            return ldt.atZone(GMT_8_ZONE).toInstant();
        }

        // 3. ZonedDateTime / OffsetDateTime
        if (obj instanceof ZonedDateTime zdt) {
            return zdt.toInstant();
        }
        if (obj instanceof OffsetDateTime odt) {
            return odt.toInstant();
        }

        // 4. java.util.Date
        if (obj instanceof java.util.Date date) {
            return date.toInstant();
        }

        // 5. Calendar
        if (obj instanceof Calendar calendar) {
            return calendar.toInstant();
        }

        // 6. Long 类型（时间戳）
        if (obj instanceof Long timestamp) {
            if (isMillisTimestamp(timestamp)) {
                return Instant.ofEpochMilli(timestamp);
            } else if (isSecondsTimestamp(timestamp)) {
                return Instant.ofEpochSecond(timestamp);
            }
            throw new IllegalArgumentException("Invalid timestamp: " + timestamp);
        }

        // 7. String 类型
        if (obj instanceof String str && !str.trim().isEmpty()) {
            // 清理字符串
            str = str.trim()
                    .replaceAll("\\s+", " ")    // 合并空白符
                    .replace('／', '/')         // 全角斜杠
                    .replace('／', '/')         // 防止复制粘贴错误
                    .replace('\u00A0', ' ')     // 非断行空格 → 普通空格
                    .strip();

            // a. 尝试解析为 Instant（支持 ISO 格式，含时区）
            try {
                return Instant.parse(str);
            } catch (DateTimeParseException ignored) {}

            // b. 依次尝试所有常见格式
            for (DateTimeFormatter formatter : COMMON_FORMATTERS) {
                try {
                    TemporalAccessor parsed = formatter.parse(str);
                    // 判断是否包含时间部分
                    if (parsed.query(TemporalQueries.localTime()) != null) {
                        // 有时间：转为 LocalDateTime
                        LocalDateTime ldt = LocalDateTime.from(parsed);
                        return ldt.atZone(GMT_8_ZONE).toInstant();
                    } else if (parsed.query(TemporalQueries.localDate()) != null) {
                        // 只有日期：当天 00:00:00
                        LocalDate ld = LocalDate.from(parsed);
                        return ld.atStartOfDay(GMT_8_ZONE).toInstant();
                    }
                } catch (DateTimeParseException ignored) {
                    // 继续尝试下一个
                }
            }

            // c. 尝试纯数字字符串（10 或 13 位）
            if (str.matches("\\d+")) {
                try {
                    long num = Long.parseLong(str);
                    if (isMillisTimestamp(num)) {
                        return Instant.ofEpochMilli(num);
                    } else if (isSecondsTimestamp(num)) {
                        return Instant.ofEpochSecond(num);
                    }
                } catch (NumberFormatException ignored) {}
            }

            // d. 特殊情况：Excel 时间（如 2024/1/24 0:00）可能因格式不匹配失败，但上面已覆盖
            //throw new IllegalArgumentException("Unsupported date string format: " + str);
            LOGGER.error("Unsupported date string format: {}", str);
            return null;
        }

        //throw new IllegalArgumentException("Cannot convert to Instant: " + obj.getClass());
        LOGGER.error("Cannot convert to Instant: {}", obj.getClass());
        return null;
    }
    /**
     * 将任意对象转为前端专用的 UTC 时间字符串格式
     * 格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * 例如：2025-08-29T16:00:00.000Z
     *
     * @param value 可为 Instant, String, Long, Date 等时间类型
     * @return 格式化后的 UTC 时间字符串；若无法解析则返回 null
     */
    public static String formatForFrontend(Object value) {
        Instant instant = toInstant(value);
        if (instant == null) {
            return null;
        }
        return ISO_8601_FOR_FRONTEND.format(instant.atZone(ZoneOffset.UTC));
    }
    /**
     * 将 Instant 转换为 java.util.Date
     *
     * @param instant 待转换的时间点，null 安全
     * @return 对应的 Date 对象；若输入为 null，则返回 null
     */
    public static Date dateFromInstant(Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    /**
     * 将 Instant 转换为 java.util.Date，输入不允许 null
     *
     * @param instant 待转换的时间点，不可为 null
     * @return 对应的 Date 对象
     * @throws IllegalArgumentException 如果 instant 为 null
     */
    public static Date dateRequireInstant(Instant instant) {
        if (instant == null) {
            throw new IllegalArgumentException("Instant 不能为 null");
        }
        return Date.from(instant);
    }

// ----------------- 辅助方法 -----------------

    /**
     * 判断 long 是否为合理的毫秒级时间戳（大致在 1970 ~ 2100 年之间）
     */
    private static boolean isMillisTimestamp(long timestamp) {
        return timestamp >= 1_000_000_000_000L && timestamp < 4_100_000_000_000L;
    }

    /**
     * 判断 long 是否为合理的秒级时间戳
     */
    private static boolean isSecondsTimestamp(long timestamp) {
        return timestamp >= 1_000_000_000L && timestamp < 4_100_000_000L;
    }

}