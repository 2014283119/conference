package com.tangshi.conferencesubscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String zb;
    private String deptid;
    private String userid;
    private String username;
    private String usergh;
    private String rzrq;
    private String tel;
    private String mobile;
    private String creat_time;
    private String update_time;
    private String position;
    private Integer ifmanager; //0否1是
    private String code;//免登授权码
}
