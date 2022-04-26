package com.tangshi.conferencesubscribe.service.impl;

import com.tangshi.common.contants.ResultCodeEnum;
import com.tangshi.common.util.ResultUtil;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.domain.UserInfo;
import com.tangshi.conferencesubscribe.mapper.db1.db1Mapper;
import com.tangshi.conferencesubscribe.mapper.db2.db2Mapper;
import com.tangshi.conferencesubscribe.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    db2Mapper db2;

    @Autowired
    db1Mapper db1;

    @Override
    public Result verifyUserId(String userId) {
        List<UserInfo> userInfoList = db2.findByUserId(userId);
        if(!listIsNotEmpty(userInfoList)){
            //为空
            return ResultUtil.fail(ResultCodeEnum.LOGIN_FAIL);
        }else {
            //不为空 从conference_manager_info表查询是否是管理员权限
            UserInfo userInfo = userInfoList.get(0);
            String username = userInfo.getUsername();
            String usergh = userInfo.getUsergh();
            int count = db1.findIfManager(username,usergh);
            //count =1 是管理员
            int ifmanager = count ==1 ? ResultCodeEnum.TRUE.getCode(): ResultCodeEnum.FALSE.getCode(); //1是管理员 0不是
            userInfo.setIfmanager(ifmanager);
            return ResultUtil.success(userInfo);
        }
    }

    @Override
    public Result findAll() {
        System.out.println("findAll() start");
        List<UserInfo> list = db2.findAll();
        System.out.println("findAll() end =====list====="+list);
        return ResultUtil.success(list);
    }

    private boolean listIsNotEmpty(List list){
        if(null != list && !list.isEmpty() && null != list.get(0)){
            return true;
        }else {
            return false;
        }
    }
}
