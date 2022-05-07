package com.tangshi.conferencesubscribe.controller;

import com.alibaba.fastjson.JSON;
import com.tangshi.common.exception.CustomException;
import com.tangshi.common.vo.Result;
import com.tangshi.common.util.ResultUtil;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import com.tangshi.conferencesubscribe.domain.UserInfo;
import com.tangshi.conferencesubscribe.dto.DingAccessTokenDTO;
import com.tangshi.conferencesubscribe.dto.DingUserIdDTO;
import com.tangshi.conferencesubscribe.service.DingAuthService;
import com.tangshi.conferencesubscribe.service.DingUserService;
import com.tangshi.conferencesubscribe.service.LoginService;
import com.tangshi.conferencesubscribe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(methods = RequestMethod.POST)
@RequestMapping(value = "/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    private DingAuthService dingAuthService;

    @Autowired
    private DingUserService dingUserService;

//    钉钉免登
    @RequestMapping("/login")
    public Result login(@RequestBody String requestBody){
        List<UserInfo> list = JSON.parseArray(requestBody, UserInfo.class);
        String code = list.get(0).getCode();
        DingAccessTokenDTO accessTokenDTO = dingAuthService.accessToken();
        DingUserIdDTO userIdDTO = dingUserService.getUserId(accessTokenDTO.getAccess_token(), code);
        String userid = userIdDTO.getUserid();
        //验证userId在DD_sys_user_int1表中是否存在 存在->允许登录
        //查询userid是否是管理员权限
        Result result = loginService.verifyUserId(userid);
        logger.debug("[钉钉] 用户免登, 根据免登授权码code, corpId获取用户信息, code: {}, corpId:{}, result:{}", code, result);
        return result;
    }

    @RequestMapping("/login2")
    public Result login2(){
        Result result = loginService.findAll();
        return result;
    }


//    查询办公地点对应的存在且启用的会议室名 cstatus=1 ifexist=1
    @RequestMapping("/queryLocaltion")
    public Result queryLocaltion(){
        Result result = userService.queryLocaltion();
        return result;
    }

//    查询当前日期下指定会议室的所有预约信息
    @RequestMapping("/queryMeetingAll")
    public Result queryMeetingAll(@RequestBody  String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = userService.queryMeetingAll(list);
        return result;
    }
//    立即预约
    @RequestMapping("/order")
    public Result order(@RequestBody  String requestBody){
        //String str="[{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室1\"},{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室2\"},{\"localtion\": \"博纳大厦\",\"meeting_name\": \"会议室3\"},{\"localtion\": \"三市\",\"meeting_name\": \"会议室1\"},{\"localtion\": \"三市\",\"meeting_name\": \"会议室2\"}]";
        List<OrderMsg> orderMsgList = JSON.parseArray(requestBody, OrderMsg.class);
        Result result;
        try {
            result = userService.order(orderMsgList);
        }catch (CustomException e){
            return ResultUtil.fail(e.getCode(),e.getMessage());
        }
        return result;
    }

//    查询当前用户名下订单列表
    @RequestMapping("/queryMeetingAllByUser")
    public Result queryMeetingAllByUser(@RequestBody  String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = userService.queryMeetingAllByUser(list);
        return result;
}

//    取消预约
    //取消整个订单 同一uuid
    @RequestMapping("/cancel")
    @ResponseBody
    public Result insertConference(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = userService.cancel(list);
        return result;
    }
}