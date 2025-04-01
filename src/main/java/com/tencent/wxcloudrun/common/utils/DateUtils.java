package com.tencent.wxcloudrun.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 将日期时间字符串转换为LocalDateTime
     * 支持 "yyyy-MM-dd" 和 "yyyy-MM-dd HH:mm:ss" 格式
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            if (dateTimeStr.length() <= 10) {
                // 如果只有日期，自动补充时间部分
                return LocalDateTime.parse(dateTimeStr + " 00:00:00", DATE_TIME_FORMATTER);
            } else {
                return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将LocalDateTime格式化为字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
