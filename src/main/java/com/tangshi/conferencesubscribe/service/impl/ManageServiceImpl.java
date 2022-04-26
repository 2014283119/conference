package com.tangshi.conferencesubscribe.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tangshi.common.contants.ResultCodeEnum;
import com.tangshi.common.util.DateUtils;
import com.tangshi.common.util.ResultUtil;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.domain.ConferenceBasic;
import com.tangshi.conferencesubscribe.domain.ConferenceDetail;
import com.tangshi.conferencesubscribe.domain.LocaltionMeetingName;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import com.tangshi.conferencesubscribe.mapper.db1.db1ManageMapper;
import com.tangshi.conferencesubscribe.mapper.db1.db1Mapper;
import com.tangshi.conferencesubscribe.service.ManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tangshi.common.contants.ResultCodeEnum.CONFERENCE_BASIC_FAIL;

@Service
public class ManageServiceImpl extends ServiceImpl<db1ManageMapper, ConferenceBasic> implements ManageService {

    @Autowired
    db1ManageMapper manageMapper;

    @Autowired
    db1Mapper db1;

    @Override
    public Result queryMeetingAll(List<OrderMsg> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            OrderMsg orderMsg = orderMsgList.get(0);
            String localtionName = orderMsg.getLocaltionName();
            String meetingName = orderMsg.getMeetingName();

            if(StringUtils.isBlank(localtionName) || StringUtils.isBlank(meetingName)){
                //前端未做非空校验,参数异常
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }

            //获取当前时间
            Date date = new Date();

            //根据办公地点 会议室名查询basic_id
            int basicId = db1.queryBasicId(localtionName,meetingName);
            //根据basic_id查询
            //查询当前有效预约记录
            EntityWrapper<ConferenceDetail> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("basic_id",basicId).eq("ifexist","1")
                        .gt("end_time",date);

            List<ConferenceDetail> list = db1.selectList(entityWrapper);
            if(null == list || list.isEmpty() || null == list.get(0)){
                return ResultUtil.success();
            }
            //同一订单处理
            List<OrderMsg> list2 = new ArrayList<>();
            for (ConferenceDetail conferenceDetail : list) {
                OrderMsg orderMsg1 = new OrderMsg();
                BeanUtils.copyProperties(conferenceDetail,orderMsg1);
                System.out.println(orderMsg1);
                orderMsg1.setBeginTime(DateUtils.DateToString(conferenceDetail.getBeginTime()));
                orderMsg1.setEndTime(DateUtils.DateToString(conferenceDetail.getEndTime()));
                list2.add(orderMsg1);
            }
            return ResultUtil.success(list2);
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result deactivate(List<OrderMsg> list) {
        return setCstatus(list,0);
    }

    @Override
    public Result enable(List<OrderMsg> list) {
        return setCstatus(list,1);
    }

    @Override
    public Result queryLocaltion() {
        List<LocaltionMeetingName> list = manageMapper.queryLocaltion();
        return ResultUtil.success(list);
    }

    @Override
    public Result delete(List<OrderMsg> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            Integer id = orderMsgList.get(0).getId();
            if(null == id){
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
            //逻辑删除
            ConferenceBasic conferenceBasic = new ConferenceBasic();
            conferenceBasic.setId(id);
            conferenceBasic.setIfexist(0);
            manageMapper.updateById(conferenceBasic);
            return ResultUtil.success();
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result insertx(List<ConferenceBasic> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            Integer maximumPeople = orderMsgList.get(0).getMaximumPeople();
            String localtionName = orderMsgList.get(0).getLocaltionName();
            String meetingName = orderMsgList.get(0).getMeetingName();
            if(StringUtils.isBlank(localtionName) || StringUtils.isBlank(meetingName) || null == maximumPeople){
                //前端未做非空校验,参数异常
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
            //先判断办公地点在location表中是否存在 不存在则添加
            Integer count = manageMapper.selectIfexist(localtionName);
            if(count == 0){
                //不存在 新增
                manageMapper.insertLocaltion(localtionName);
            }
            int localtionId = manageMapper.selectIdByLocaltion(localtionName);
            //判断 会议室名是否存在
            //不存在 新增
            Integer count2 = manageMapper.selectMeetingIfexist(localtionName, meetingName);
            if(count2 == 0){
                //不存在 新增
                ConferenceBasic conferenceBasic = new ConferenceBasic();
                conferenceBasic.setLocaltionName(localtionName);
                conferenceBasic.setMeetingName(meetingName);
                conferenceBasic.setMaximumPeople(maximumPeople);
                conferenceBasic.setCstatus(1);
                conferenceBasic.setIfexist(1);
                conferenceBasic.setLocaltionId(localtionId);
                manageMapper.insert(conferenceBasic);
                return ResultUtil.success();
            }
            //存在 结束
            return ResultUtil.fail(CONFERENCE_BASIC_FAIL);
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result modify(List<ConferenceBasic> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            Integer id = orderMsgList.get(0).getId();
            Integer maximumPeople = orderMsgList.get(0).getMaximumPeople();
            //String meetingName = orderMsgList.get(0).getMeetingName();
            if(null == id || null == maximumPeople ){
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }

            //TODO
            //判定meetingName重名的情况
            manageMapper.updateById(orderMsgList.get(0));
            return ResultUtil.success();
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result queryManagerAll() {
        List<OrderMsg> list= db1.selectManagerAll();
        return ResultUtil.success(list);
    }

    @Override
    public Result addManager(List<OrderMsg> list) {
        if(listIsNotEmpty(list)){
            //不为空
            OrderMsg orderMsg = list.get(0);
            String username = orderMsg.getUsername();
            String usergh = orderMsg.getUsergh();
            //查询用户名
            int count = db1.queryIfExist(username, usergh);
            if(count == 0){
                //用户名工号不存在 允许新增
                db1.addManager(username,usergh);
                return ResultUtil.success();
            }else {
                //用户名工号存在 不允许新增
                return ResultUtil.fail(ResultCodeEnum.USER_INFO_EXIST_FAIL);
            }
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result deleteManager(List<OrderMsg> list) {
        if(listIsNotEmpty(list)){
            OrderMsg orderMsg = list.get(0);
            String username = orderMsg.getUsername();
            String usergh = orderMsg.getUsergh();
            db1.deleteManager(username,usergh);
            return ResultUtil.success();
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }


    private Result setCstatus(List<OrderMsg> orderMsgList, int cstatus) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            OrderMsg orderMsg = orderMsgList.get(0);
            Integer id = orderMsg.getId();

            if(null == id){
                //前端未做非空校验,参数异常
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
            ConferenceBasic conferenceBasic = new ConferenceBasic();
            conferenceBasic.setCstatus(cstatus);
            conferenceBasic.setId(id);
            manageMapper.updateById(conferenceBasic);
            return ResultUtil.success();
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    private boolean listIsNotEmpty(List list){
        if(null != list && !list.isEmpty() && null != list.get(0)){
            return true;
        }else {
            return false;
        }
    }
}
