package com.cjyun.tb.supervisor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class TimeUtils {

    private Date parse;

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurTime() {
        Date curDate = new Date(System.currentTimeMillis());
        long curtime = curDate.getTime();
        return curtime;
    }

    /**
     * 获取month个月前的时间的毫秒值
     *
     * @param month 对应的多少个月以前
     * @return
     * @throws ParseException
     */
    public static long getTrimester(int month) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -month);
        String format = sdf.format(calendar.getTime());
        Date parse1 = sdf.parse(format);
        long contrastTime = parse1.getTime();

        return contrastTime;
    }

    /**
     * 获取标准Date类型的年月日
     *
     * @param date
     * @return
     */
    public static String getDate(String date) {

        return date.substring(0, 10);
    }

    /**
     * 获取输入时间的毫秒值
     *
     * @param date
     * @return
     */
    public static long getDateLong(String  date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

      //  String format = sdf.format(date);
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = parse.getTime();
        return time;
    }

}
