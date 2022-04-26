package com.tangshi.conferencesubscribe.controller;

import com.alibaba.fastjson.JSON;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.domain.ConferenceBasic;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import com.tangshi.conferencesubscribe.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(methods = RequestMethod.POST)
@RequestMapping(value = "/manager")
public class ManageController {
    @Autowired
    ManageService manageService;

    //查询办公地点对应的会议室
    //TODO
    @RequestMapping("/queryLocaltion")
    public Result queryLocaltion(){
        return manageService.queryLocaltion();
    }

    //查询当天之后该会议室所有有效预约记录回显通知客户
    //入参 办公地点及会议室名
    @RequestMapping("/queryMeetingAllByDate")
    public Result queryMeetingAllByDate(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.queryMeetingAll(list);
        return result;
    }

    //停用会议室
    //入参 id
    @RequestMapping("/deactivate")
    public Result deactivate(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.deactivate(list);
        return result;
    }

    //启用会议室
    //入参 id
    @RequestMapping("/enable")
    public Result enable(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.enable(list);
        return result;
    }

    //删除会议室 ifexist=0
    //入参 id
    @RequestMapping("/delete")
    public Result delete(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.delete(list);
        return result;
    }

    //增加会议室
    //入参 办公地点 会议室名
    @RequestMapping("/insert")
    public Result insert(@RequestBody String requestBody){
        List<ConferenceBasic> list = JSON.parseArray(requestBody, ConferenceBasic.class);
        Result result = manageService.insertx(list);
        return result;
    }

    //修改会议室信息 包括停用启用 会议室名
    //入参 id 会议室名 maximumPeople
    @RequestMapping("/modify")
    public Result modify(@RequestBody String requestBody){
        List<ConferenceBasic> list = JSON.parseArray(requestBody, ConferenceBasic.class);
        Result result = manageService.modify(list);
        return result;
    }

    //添加会议室管理人员
    //入参 username usergh
    @RequestMapping("/addManager")
    public Result addManager(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.addManager(list);
        return result;
    }

    //查看会议室管理员列表
    //查看所有管理员的usergh username
    @RequestMapping("/queryManagerAll")
    public Result queryManagerAll(){
        Result result = manageService.queryManagerAll();
        return result;
    }

    //删除会议室管理员
    //入参 username usergh
    @RequestMapping("/deleteManager")
    public Result deleteManager(@RequestBody String requestBody){
        List<OrderMsg> list = JSON.parseArray(requestBody, OrderMsg.class);
        Result result = manageService.deleteManager(list);
        return result;
    }
}
