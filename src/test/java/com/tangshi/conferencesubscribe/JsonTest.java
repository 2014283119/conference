package com.tangshi.conferencesubscribe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
    class Student {
    private String name;
    private String gender;
    private int age;
}

/**
 * @program: demo
 * @description: jsonString jsonArray jsonObject List<Object> 转换
 * @copyRight:Sunyard
 * @author: ZhuGaoPo
 * @version:1.0
 * @create: 2019-12-06 09:49
 */
public class JsonTest {
    public static void main(String[] args) {
        Student s1 = new Student();
        Student s2 = new Student();
        Student s3 = new Student();
        s1.setName("小明").setAge(18).setGender("男");
        s2.setName("小红").setAge(16).setGender("女");
        s3.setName("小刚").setAge(19).setGender("男");
        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        System.out.println("List<Student>为："+ students);

        //fastjson  List<Object> => JSONArray
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(students));
        System.out.println("fastjson  List<Object> 转 JSONArray：" + jsonArray);
        //jsonString
        String jsonString = jsonArray.toJSONString();
        // String jsonString = JSONArray.toJSONString(students);
        System.out.println("JSONString 为：" + jsonString);
        //fastJSon  JSONArray =>List<Object>
        List<Student> list = JSONObject.parseArray(jsonArray.toJSONString(),Student.class);
        System.out.println("fastjson  JSONArray 转 List<Object>：" + list);
        //jsonString 字符串 转 list
        List<Student> list1 = JSONObject.parseArray(jsonString,Student.class);
        System.out.println("fastjson  jsonString 转 List<Object>：" + list1);
    }
}

