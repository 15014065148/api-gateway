package com.eveb.gateway.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {

    static SimpleDateFormat sdf_RFC3339=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-04:00'");

    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String ConventDate_RFC3339(Date d){
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR_OF_DAY,-12);
        return sdf_RFC3339.format(cal.getTime());
    }

    public static String ConventDate(Date d){
        return sdf.format(d);
    }

    public static String getUTCTimeStr() {
        java.text.DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int millisecond = cal.get(Calendar.MILLISECOND);
        UTCTimeBuffer.append(year).append("-").append(month).append("-").append(day);
        UTCTimeBuffer.append(" ").append(hour).append(":").append(minute).append(":").append(second).append(".")
                .append(millisecond);
        try {
            format.parse(UTCTimeBuffer.toString());
            return UTCTimeBuffer.toString() + " UTC";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
