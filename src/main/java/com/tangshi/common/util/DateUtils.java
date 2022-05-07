package com.tangshi.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**

     * 短格式日期格式化

     */

    private static final SimpleDateFormat SHORT_YEAR_FORMATOR = new SimpleDateFormat("yy-MM-dd");

    /**

     * 标准日期格式化

     */

    private static final SimpleDateFormat CUSTOM_DAY_FORMATOR = new SimpleDateFormat("yyyy-MM-dd");

    /**

     * 标准日期时间格式化

     */

    public static String DateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }
    public static Date StrToDate(String str){
        Date date = null;
        try{
            date = StringToDate(str);
        }catch (ParseException e){
        }
        return date;
    }

    private static Date StringToDate(String str) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(str);
        return date;
    }

    //获取前(后)一天的日期 int=1 为后一天
    public static Date getDate(String specifiedDay, int diffDays) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(SHORT_YEAR_FORMATOR.parse(specifiedDay));
            c.set(Calendar.DATE, c.get(Calendar.DATE) + diffDays);
            return CUSTOM_DAY_FORMATOR.parse(CUSTOM_DAY_FORMATOR.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
