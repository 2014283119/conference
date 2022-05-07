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
                //前端未做非空校验,参数异常
                return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
            }
            //根据办公地点,会议名称,查出对应的basic_id
            int basicId = db1.queryBasicId(localtion_name,meeting_name);
            //生成统一订单号UUID
            String uuid = UUID.randomUUID().toString();
            for (OrderMsg orderMsg : orderMsgList) {
                //开启事务
               // DataSourceTransactionManager manager = new DataSourceTransactionManager();

                //beginTime需要小于endTime

                ConferenceDetail conferenceDetail = new ConferenceDetail();
                //填充数据
                BeanUtils.copyProperties(orderMsg, conferenceDetail);
                System.out.println(conferenceDetail);
                //时间转换
                String beginTime = orderMsg.getBeginTime();
                String endTime = orderMsg.getEndTime();
                if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
                    //前端未做非空校验,参数异常
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }
                //开始时间不能大于结束时间
                Date beginDate = DateUtils.StrToDate(beginTime);
                Date endDate = DateUtils.StrToDate(endTime);
                int i = beginDate.compareTo(endDate);
                if(i >= 0){
                    //开始时间大于等于结束时间 前端未做校验,报错
                    return ResultUtil.fail(ResultCodeEnum.ENDTIME_LT_BEGINTIME_ERROR);
                }

                conferenceDetail.setBasicId(basicId);
                conferenceDetail.setBeginTime(beginDate);
                conferenceDetail.setEndTime(endDate);
                conferenceDetail.setUuid(uuid);
                conferenceDetail.setIfexist("1");

                //判断时间性冲突
                int noconflictcount = db1.ifconflict(basicId, beginDate, endDate);
                int allCount = db1.findAllByBasicId(basicId);
                if(allCount == 0){
                    //当前basic_id无预定 不会产生时间性冲突 直接新增
                    db1.insert(conferenceDetail);
                }else {
                    //当前basic_id 数据库中已存在数据 可能发生时间性冲突
                    //不冲突的count等于总count 冲突count=0 新增
                    if(noconflictcount == allCount){
                        db1.insert(conferenceDetail);
                    }else if(noconflictcount < allCount){
                        //存在冲突的count
                        //事务回滚
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
                String dateStr = orderMsg.getBeginTime();//beginTime当做当前日期
                String localtionName = orderMsg.getLocaltionName();
                String meetingName = orderMsg.getMeetingName();

                if(StringUtils.isBlank(dateStr) || dateStr.length() != 16){
                    //前端未做非空校验,参数异常
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }

                if(StringUtils.isBlank(localtionName) || StringUtils.isBlank(meetingName)){
                    //前端未做非空校验,参数异常
                    return ResultUtil.fail(ResultCodeEnum.PARAMETER_LACK_FAIL);
                }

                //取当前日期和后一天日期的0点
                //2022-07-31 13:26:32
                String date = dateStr.substring(0, 11) + "00:00:00";
                Date dateTime = DateUtils.StrToDate(date);
                Date afterDateTime = DateUtils.getDate(date, 1);

                //根据办公地点 会议室名查询basic_id
                int basicId = db1.queryBasicId(localtionName,meetingName);
                //根据basic_id查询
                EntityWrapper<ConferenceDetail> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("basic_id",basicId).eq("ifexist","1")
                        .gt("begin_time",dateTime).le("end_time",afterDateTime);

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
            //查询未过期 启用状态信息
            List<OrderMsg> list2 = queryValidOrInvalid(0, 1, usergh);
            DateMsgResult result = new DateMsgResult("待参加",list2);
            list.add(result);

            //查询未过期 停用状态信息
            List<OrderMsg> list3 = queryValidOrInvalid(0, 0, usergh);
            DateMsgResult result2 = new DateMsgResult("暂时无法参加",list3);
            list.add(result2);

            //查询过期信息
            List<OrderMsg> list4 = queryValidOrInvalid(1, -1, usergh);
            DateMsgResult result3 = new DateMsgResult("过期",list4);
            list.add(result3);

            return ResultUtil.success(list);

//            //查询启用basic_id
//            List<OrderMsg> list = queryValidOrInvalid(1, usergh);
//            //查询停用basic_id
//            List<OrderMsg> list2 = queryValidOrInvalid(0, usergh);
//            HashMap<String,List<OrderMsg>> hashMap = new HashMap<>();
//            hashMap.put("valid",list);
//            hashMap.put("invalid",list2);
//            return ResultUtil.success(hashMap);
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
                conferenceDetail.setIfexist("0");//0否1是
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

    //validstatus 过期状态 0未过期 1 过期
    //cstatus 停用状态 0停用 1启用
    private List<OrderMsg> queryValidOrInvalid(int validstatus, int cstatus, String usergh){
        EntityWrapper<ConferenceDetail> wrapper = new EntityWrapper<>();
        wrapper.eq("usergh",usergh).eq("ifexist","1");
        //查询当前时间之后的有效订单
        Date date = new Date();
        //TODO 测试
        String testDateStr = "2022-04-07 17:00";
        Date testDate = DateUtils.StrToDate(testDateStr);
        if(validstatus == 0){
            //未过期
            wrapper.gt("end_time",testDate);
        }else if (validstatus ==1){
            //过期
            wrapper.lt("end_time",testDate);
        }
        if(cstatus == 0 || cstatus ==1){
            List<Integer> validBasicIds = db1.queryValidOrInvalidBasicIds(cstatus);
            //停用的会议室为空时,返空,不查询
            if(validBasicIds.size() == 0){
                return null;
            }
            wrapper.in("basic_id", validBasicIds);
            //升序
            wrapper.orderBy("begin_time",true);
        }
        if(cstatus == -1){
            //降序
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
            //查询会议室地点 会议室名
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
