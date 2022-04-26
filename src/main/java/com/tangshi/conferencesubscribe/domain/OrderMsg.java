package com.tangshi.conferencesubscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMsg {
    private Integer id;
    private String localtionName; //办公地点
    private String meetingName;    //会议名称
    private String username;    //申请人
    private String usergh;  //用户工号
    private String yyyt;    //预约用途
    private String beginTime;    //(预约)开始时间
    private String endTime;  //结束时间
    private String uuid;
    private String temporalInterval;   //前端标记(不用管)
}
