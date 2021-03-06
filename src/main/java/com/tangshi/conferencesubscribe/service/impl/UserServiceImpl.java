package com.tangshi.conferencesubscribe.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tangshi.common.contants.ResultCodeEnum;
import com.tangshi.common.exception.CustomException;
import com.tangshi.common.util.DateUtils;
import com.tangshi.common.util.ResultUtil;
import com.tangshi.common.vo.DateMsgResult;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.dao.UserDao;
import com.tangshi.conferencesubscribe.domain.*;
import com.tangshi.conferencesubscribe.mapper.db1.db1Mapper;
import com.tangshi.conferencesubscribe.mapper.db2.db2Mapper;
import com.tangshi.conferencesubscribe.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<db1Mapper, ConferenceDetail> implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    db1Mapper db1;

    @Autowired
    db2Mapper db2;

    @Override
    public Result queryLocaltion() {
        List<LocaltionMeetingName> list = db1.queryLocaltion();
        return ResultUtil.success(list);
    }

    @Override
    @Transactional(rollbackFor = CustomException.class)
    public Result order(List<OrderMsg> orderMsgList){

        long startTime=System.currentTimeMillis();
        System.out.println(startTime);

        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){

            String localtion_name = orderMsgList.get(0).getLocaltionName();
            String meeting_name = orderMsgList.get(0).getMeetingName();
            String username = orderMsgList.get(0).getUsername();
            String usergh = orderMsgList.get(0).getUsergh();
            if(StringUtils.isBlank(localtion_name) || StringUtils.isBlank(meeting_name)
                || StringUtils.isBlank(username) || StringUtils.isBlank(usergh)){
                //????????????????????????,????????????
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
            //??????????????????,????????????,???????????????basic_id
            int basicId = db1.queryBasicId(localtion_name,meeting_name);
            //?????????????????????UUID
            String uuid = UUID.randomUUID().toString();
            for (OrderMsg orderMsg : orderMsgList) {
                //????????????
               // DataSourceTransactionManager manager = new DataSourceTransactionManager();

                //beginTime????????????endTime

                ConferenceDetail conferenceDetail = new ConferenceDetail();
                //????????????
                BeanUtils.copyProperties(orderMsg, conferenceDetail);
                System.out.println(conferenceDetail);
                //????????????
                String beginTime = orderMsg.getBeginTime();
                String endTime = orderMsg.getEndTime();
                if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
                    //????????????????????????,????????????
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }
                //????????????????????????????????????
                Date beginDate = DateUtils.StrToDate(beginTime);
                Date endDate = DateUtils.StrToDate(endTime);
                int i = beginDate.compareTo(endDate);
                if(i >= 0){
                    //???????????????????????????????????? ??????????????????,??????
                    return ResultUtil.fail(ResultCodeEnum.ENDTIME_LT_BEGINTIME_ERROR);
                }

                conferenceDetail.setBasicId(basicId);
                conferenceDetail.setBeginTime(beginDate);
                conferenceDetail.setEndTime(endDate);
                conferenceDetail.setUuid(uuid);
                conferenceDetail.setIfexist("1");

                //?????????????????????
                int noconflictcount = db1.ifconflict(basicId, beginDate, endDate);
                int allCount = db1.findAllByBasicId(basicId);
                if(allCount == 0){
                    //??????basic_id????????? ??????????????????????????? ????????????
                    db1.insert(conferenceDetail);
                }else {
                    //??????basic_id ??????????????????????????? ???????????????????????????
                    //????????????count?????????count ??????count=0 ??????
                    if(noconflictcount == allCount){
                        db1.insert(conferenceDetail);
                    }else if(noconflictcount < allCount){
                        //???????????????count
                        //????????????
                        throw new CustomException(ResultCodeEnum.TIME_CONFLICT_ERROR.getCode(),
                                            beginTime+"-"+endTime+ResultCodeEnum.TIME_CONFLICT_ERROR.getMessage());
                    }
                }
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result queryMeetingAll(List<OrderMsg> orderMsgList) {
        List<DateMsgResult> list3 = new ArrayList<>();
        for (OrderMsg orderMsg : orderMsgList) {
            if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsg){
                String dateStr = orderMsg.getBeginTime();//beginTime??????????????????
                String localtionName = orderMsg.getLocaltionName();
                String meetingName = orderMsg.getMeetingName();

                if(StringUtils.isBlank(dateStr) || dateStr.length() != 16){
                    //????????????????????????,????????????
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }

                if(StringUtils.isBlank(localtionName) || StringUtils.isBlank(meetingName)){
                    //????????????????????????,????????????
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }

                //????????????????????????????????????0???
                //2022-07-31 13:26:32
                String date = dateStr.substring(0, 11) + "00:00:00";
                Date dateTime = DateUtils.StrToDate(date);
                Date afterDateTime = DateUtils.getDate(date, 1);

                //?????????????????? ??????????????????basic_id
                int basicId = db1.queryBasicId(localtionName,meetingName);
                //??????basic_id??????
                EntityWrapper<ConferenceDetail> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("basic_id",basicId).eq("ifexist","1")
                        .gt("begin_time",dateTime).le("end_time",afterDateTime);

                List<ConferenceDetail> list = db1.selectList(entityWrapper);
                if(null == list || list.isEmpty() || null == list.get(0)){
                    return ResultUtil.success();
                }
                //??????????????????
                List<OrderMsg> list2 = new ArrayList<>();
                for (ConferenceDetail conferenceDetail : list) {
                    OrderMsg orderMsg1 = new OrderMsg();
                    BeanUtils.copyProperties(conferenceDetail,orderMsg1);
                    System.out.println(orderMsg1);
                    orderMsg1.setBeginTime(DateUtils.DateToString(conferenceDetail.getBeginTime()));
                    orderMsg1.setEndTime(DateUtils.DateToString(conferenceDetail.getEndTime()));
                    list2.add(orderMsg1);
                }
                DateMsgResult result = new DateMsgResult();
                result.setDate(date);
                result.setOrderList(list2);
                list3.add(result);
            }
            else {
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
        }
        return ResultUtil.success(list3);
    }

    @Override
    public Result queryMeetingAllByUser(List<OrderMsg> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            String usergh = orderMsgList.get(0).getUsergh();


            List<DateMsgResult> list = new ArrayList<>();
            //??????????????? ??????????????????
            List<OrderMsg> list2 = queryValidOrInvalid(0, 1, usergh);
            DateMsgResult result = new DateMsgResult("?????????",list2);
            list.add(result);

            //??????????????? ??????????????????
            List<OrderMsg> list3 = queryValidOrInvalid(0, 0, usergh);
            DateMsgResult result2 = new DateMsgResult("??????????????????",list3);
            list.add(result2);

            //??????????????????
            List<OrderMsg> list4 = queryValidOrInvalid(1, -1, usergh);
            DateMsgResult result3 = new DateMsgResult("??????",list4);
            list.add(result3);

            return ResultUtil.success(list);
        }
        return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
    }

    @Override
    public Result cancel(List<OrderMsg> orderMsgList) {
        if(null != orderMsgList && !orderMsgList.isEmpty() && null != orderMsgList.get(0)){
            for (OrderMsg orderMsg : orderMsgList) {
                Integer id = orderMsg.getId();
                ConferenceDetail conferenceDetail = new ConferenceDetail();
                conferenceDetail.setId(id);
                conferenceDetail.setIfexist("0");//0???1???
                db1.updateById(conferenceDetail);
            }
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

    //validstatus ???????????? 0????????? 1 ??????
    //cstatus ???????????? 0?????? 1??????
    private List<OrderMsg> queryValidOrInvalid(int validstatus, int cstatus, String usergh){
        EntityWrapper<ConferenceDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("usergh",usergh).eq("ifexist","1");
        //???????????????????????????????????????
        Date date = new Date();
        //TODO ??????
        String testDateStr = "2022-04-07 17:00";
        Date testDate = DateUtils.StrToDate(testDateStr);
        if(validstatus == 0){
            //?????????
            wrapper.gt("end_time",testDate);
        }else if (validstatus ==1){
            //??????
            wrapper.lt("end_time",testDate);
        }
        if(cstatus == 0 || cstatus ==1){
            List<Integer> validBasicIds = db1.queryValidOrInvalidBasicIds(cstatus);
            //???????????????????????????,??????,?????????
            if(validBasicIds.size() == 0){
                return null;
            }
            wrapper.in("basic_id", validBasicIds);
            //??????
            wrapper.orderBy("begin_time",true);
        }
        if(cstatus == -1){
            //??????
            wrapper.orderBy("begin_time",false);
        }
        List<ConferenceDetail> list = db1.selectList(wrapper);
        if (null == list || list.isEmpty() || null == list.get(0)) {
            return null;
        }
        List<OrderMsg> list2 = new ArrayList<>();
        for (ConferenceDetail conferenceDetail : list) {
            Integer basicId = conferenceDetail.getBasicId();
            OrderMsg orderMsg = new OrderMsg();
            BeanUtils.copyProperties(conferenceDetail, orderMsg);
            //????????????????????? ????????????
            ConferenceBasic conferenceBasic = db1.selectLocalMeetingById(basicId);
            System.out.println(orderMsg);
            orderMsg.setBeginTime(DateUtils.DateToString(conferenceDetail.getBeginTime()));
            orderMsg.setEndTime(DateUtils.DateToString(conferenceDetail.getEndTime()));
            orderMsg.setLocaltionName(conferenceBasic.getLocaltionName());
            orderMsg.setMeetingName(conferenceBasic.getMeetingName());
            list2.add(orderMsg);
        }
        return list2;
    }
}
