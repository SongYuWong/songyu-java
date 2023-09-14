package com.songyu.commonutils;

import com.songyu.commonutils.exception.DateFormatIllegalException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 通用日期工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 15:10
 */
public class CommonDateUtils {

    /**
     * 校验日期格式
     *
     * @param dateStr 日期时间字符串
     * @param format  日期时间格式串
     * @return 日期时间串是否服务所给的日期时间格式串
     * @throws DateFormatIllegalException 日期时间格式串不符合标准规范
     */
    public static boolean checkDateFormat(String dateStr, String format) throws DateFormatIllegalException {
        SimpleDateFormat dateFormat;
        try {
            dateFormat = new SimpleDateFormat(format);
        } catch (IllegalArgumentException e) {
            throw new DateFormatIllegalException("非法的日期格式串");
        }
        return checkDateFormat(dateStr, dateFormat);
    }

    /**
     * 校验日期格式
     *
     * @param dateStr    日期时间字符串
     * @param dateFormat 日期时间格式串
     * @return 日期时间串是否服务所给的日期时间格式串
     */
    public static boolean checkDateFormat(String dateStr, SimpleDateFormat dateFormat) {
        try {
            Date ignore = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 日期加减时间单位值（负值表示减）
     *
     * @param date     被加减的日期时间
     * @param times    时间单位值
     * @param timeUnit 时间单位
     * @return 结果日期时间值
     */
    public static Date addTimes(Date date, long times, TimeUnit timeUnit) {
        return new Date(date.getTime() + timeUnit.toMillis(times));
    }

    /**
     * 时间日期加减小时值
     *
     * @param date  时间日期
     * @param hours 小时值
     * @return 结果日期时间值
     */
    public static Date addHours(Date date, long hours) {
        return addTimes(date, hours, TimeUnit.HOURS);
    }

    /**
     * 日期时间转格式化日期时间串
     *
     * @param date 日期时间
     * @return 日期时间串
     */
    public static String toString(Date date) {
        try {
            return toString(date, "yyyy-MM-dd HH:mm:ss");
        } catch (DateFormatIllegalException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期时间转格式化日期时间串
     *
     * @param date       日期时间
     * @param dateformat 格式串
     * @return 日期时间串
     * @throws DateFormatIllegalException 非法的日期格式串
     */
    public static String toString(Date date, String dateformat) throws DateFormatIllegalException {
        SimpleDateFormat dateFormat;
        try {
            dateFormat = new SimpleDateFormat(dateformat);
        } catch (IllegalArgumentException e) {
            throw new DateFormatIllegalException("日期时间格式不合法");
        }
        return dateFormat.format(date);
    }



}
