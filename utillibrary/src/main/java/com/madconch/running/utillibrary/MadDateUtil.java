package com.madconch.running.utillibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 功能描述:Date转换工具
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class MadDateUtil {
    //示例:2017/05/20 14:10:30 星期六
    public enum TimeFormat {
        YEAR("yyyy"),//2017
        MONTH("M"),//5
        DAY("d"),//20
        HOUR("h"),//2
        HOUR24("H"),//14
        MINUTE("m"),//10
        SECOND("s"),//30
        WEEK("E"),//星期六
        AM_PM("a"),//下午

        L_MONTH_DAY("M-dd"),//5-20
        B_MONTH_DAY("M/dd"),//5/20
        C_MONTH_DAY("M月dd日"),//5月20日
        P_MONTH_DAY("M.dd"),//5.20

        L_YEAR_MONTH("yyyy-MM"),//2017-05
        B_YEAR_MONTH("yyyy/MM"),//2017/05
        C_YEAR_MONTH("yyyy年MM月"),//2017年05月
        P_YEAR_MONTH("yyyy.MM"),//2017.05

        L_YEAR_MONTH_DAY("yyyy-MM-dd"),//2017-05-20
        B_YEAR_MONTH_DAY("yyyy/MM/dd"),//2017/05/20
        C_YEAR_MONTH_DAY("yyyy年MM月dd日"),//2017年05月20日
        P_YEAR_MONTH_DAY("yyyy.MM.dd"),//2017.05.20

        L_YEAR_MONTH_DAY_S_WEEK("yyyy-MM-dd E"),//2017-05-20 星期六
        B_YEAR_MONTH_DAY_S_WEEK("yyyy/MM/dd E"),//2017/05/20 星期六
        C_YEAR_MONTH_DAY_S_WEEK("yyyy年MM月dd日 E"),//2017年05月20日 星期六
        P_YEAR_MONTH_DAY_S_WEEK("yyyy.MM.dd E"),//2017.05.20 星期六

        L_YEAR_MONTH_DAY_S_AP("yyyy-MM-dd a"),//2017-05-20 下午
        B_YEAR_MONTH_DAY_S_AP("yyyy/MM/dd a"),//2017/05/20 下午
        C_YEAR_MONTH_DAY_S_AP("yyyy年MM月dd日 a"),//2017年05月20日 下午
        P_YEAR_MONTH_DAY_S_AP("yyyy.MM.dd a"),//2017.05.20 下午

        C_YEAR_MONTH_DAY_S_AP_SC_HOUR("yyyy年MM月dd日 a hh时"),//2017年05月20日 14时

        L_YEAR_MONTH_DAY_S_HOUR24("yyyy-MM-dd H"),//2017-05-20 14
        B_YEAR_MONTH_DAY_S_HOUR24("yyyy/MM/dd H"),//2017/05/20 14
        C_YEAR_MONTH_DAY_S_HOUR24("yyyy年MM月dd日 H时"),//2017年05月20日 14时
        P_YEAR_MONTH_DAY_S_HOUR24("yyyy.MM.dd H"),//2017.05.20 14

        C_YEAR_MONTH_DAY_S_AP_SC_HOUR_MINUTE("yyyy年MM月dd日 a hh时mm分"),//2017年05月20日 下午 14时10分

        L_YEAR_MONTH_DAY_SN_HOUR24_MINUTE("yyyy-MM-dd HH:mm"),//2017-05-20 14:10
        B_YEAR_MONTH_DAY_SN_HOUR24_MINUTE("yyyy/MM/d HH:mm"),//2017/05/20 14:10
        C_YEAR_MONTH_DAY_SN_HOUR24_MINUTE("yyyy年MM月d日 HH:mm"),//2017年05月20日 14:10
        C_YEAR_MONTH_DAY_SC_HOUR24_MINUTE("yyyy年MM月d日 HH时mm分"),//2017年05月20日 14时10分
        P_YEAR_MONTH_DAY_SN_HOUR24_MINUTE("yyyy.MM.dd HH:mm"),//2017.05.20 14:10

        C_YEAR_MONTH_DAY_S_AP_SC_HOUR_MINUTE_SECOND("yyyy年MM月dd日 a hh时mm分ss秒"),//2017年05月20日 下午 14时10分30秒

        L_YEAR_MONTH_DAY_SN_HOUR24_MINUTE_SECOND("yyyy-MM-dd HH:mm:ss"),//2017-05-20 14:10:30
        B_YEAR_MONTH_DAY_SN_HOUR24_MINUTE_SECOND("yyyy/MM/d HH:mm:ss"),//2017/05/20 14:10:30
        C_YEAR_MONTH_DAY_SN_HOUR24_MINUTE_SECOND("yyyy年MM月d日 HH:mm:ss"),//2017年05月20日 14:10:30
        C_YEAR_MONTH_DAY_SC_HOUR24_MINUTE_SECOND("yyyy年MM月d日 HH时mm分ss秒"),//2017年05月20日 14时10分30秒
        P_YEAR_MONTH_DAY_SN_HOUR24_MINUTE_SECOND("yyyy.MM.dd HH:mm:ss"),//2017.05.20 14:10:30
        ;
        public String formatStr;

        TimeFormat(String formatStr) {
            this.formatStr = formatStr;
        }
    }

    public static Date toDate(String srcFormat, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormat, Locale.CHINA);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date toDate(TimeFormat srcFormat, String date) {
        return toDate(srcFormat.formatStr, date);
    }

    public static Date toDate(long date) {
        return new Date(date);
    }

    public static Date toDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static String toString(String destFormat, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(destFormat, Locale.getDefault());
        return sdf.format(date);
    }

    public static String toString(TimeFormat destFormat, Date date) {
        return toString(destFormat.formatStr, date);
    }

    public static String toString(String srcFormat, String destFormat, String date) {
        Date dt = toDate(srcFormat, date);
        if (dt != null) {
            return toString(destFormat, dt);
        }

        return "";
    }

    public static String toString(TimeFormat srcFormat, String destFormat, String date) {
        return toString(srcFormat.formatStr, destFormat, date);
    }

    public static String toString(String srcFormat, TimeFormat destFormat, String date) {
        return toString(srcFormat, destFormat.formatStr, date);
    }

    public static String toString(TimeFormat srcFormat, TimeFormat destFormat, String date) {
        return toString(srcFormat.formatStr, destFormat.formatStr, date);
    }

    public static String toString(String destFormat, long date) {
        Date dt = toDate(date);
        return toString(destFormat, dt);
    }

    public static String toString(TimeFormat destFormat, long date) {
        return toString(destFormat.formatStr, date);
    }

    @Deprecated
    public static Date toGuessDate(String date) {
        try {
            return new Date(Date.parse(date));
        } catch (Exception e) {
            //ignore
        }

        TimeFormat[] vs = TimeFormat.values();
        for (TimeFormat format : vs) {
            Date dt = toDate(format, date);
            if (dt != null) {
                return dt;
            }
        }

        return null;
    }
}
