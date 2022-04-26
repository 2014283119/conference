package com.tangshi.conferencesubscribe;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tangshi.conferencesubscribe.domain.ConferenceDetail;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import com.tangshi.conferencesubscribe.mapper.db1.db1Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class test1 {

    @Autowired
    db1Mapper db1;

    @Test
    public void test1(){
        String str="[{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室1\"},{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室2\"},{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室3\"},{\"localtion\": \"三市\",\"meeting_name\": \"会议室1\"},{\"localtion\": \"三市\",\"meeting_name\": \"会议室2\"}]";
        JSONArray json=JSONArray.parseArray(str);

        for (Object o : json) {

        }
    }

    @Test
    public void test2(){
        //List<OrderMsg> list = null;
        //System.out.println("list: "+list.isEmpty());
        List<OrderMsg> list2 = new ArrayList<>();
        System.out.println("list2: "+list2.isEmpty());
        List<OrderMsg> list3 = new ArrayList<>();
        OrderMsg orderMsg = null;
        list3.add(orderMsg);
        System.out.println("list3: "+list3.isEmpty());
    }

    @Test
    public void  test3(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        System.out.println(dateString);
    }

    @Test
    public void test4() throws ParseException {
        // 将date字符串转化为日期
        String dateString = "2018-08-08 08:30:32";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(dateString);
        System.out.println(date.toString());
    }

    @Test
    public void test5(){
        String beginTime = "2018-07-28 13:26:32";
        String endTime = "2018-07-28 12:26:32";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date date1 = format.parse(beginTime);
            Date date2 = format.parse(endTime);
            System.out.println(date1);
            System.out.println(date2);
            int compareTo = date1.compareTo(date2);

            System.out.println(compareTo);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6(){
        long startTime=System.currentTimeMillis();
        System.out.println(startTime);
        long endTime=System.currentTimeMillis();
        System.out.println(endTime);
    }

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

    private static final SimpleDateFormat CUSTOM_DAYTIME_FORMATOR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Date getDate(String specifiedDay, int diffDays) {

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

    @Test
    public void test7(){
        String dateTime = "2022-07-31 13:26:32";
        String date = dateTime.substring(0, 11) + "00:00:00";
        System.out.println(date);
        Date date1 = getDate(date, 1);
        System.out.println(date1);
    }

    @Test
    public void test8(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(date);
        System.out.println(dateFormat.format(date));
    }

}
