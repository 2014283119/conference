package com.tangshi.conferencesubscribe.mapper.db1;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tangshi.conferencesubscribe.anno.db1Dao;
import com.tangshi.conferencesubscribe.domain.ConferenceBasic;
import com.tangshi.conferencesubscribe.domain.ConferenceDetail;
import com.tangshi.conferencesubscribe.domain.LocaltionMeetingName;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@db1Dao
@Mapper
public interface db1Mapper extends BaseMapper<ConferenceDetail> {
    List<ConferenceDetail> findAll();

    List<LocaltionMeetingName> queryLocaltion();

    int queryBasicId(@Param("localName") String localtion_name,
                     @Param("meetingName") String meeting_name);

    List<ConferenceDetail> queryMeetingAll(@Param("basicId") Integer basicId);


    int ifconflict(@Param("basicId") Integer basicId,
                   @Param("beginTime") Date beginDate,
                   @Param("endTime") Date endDate);

    int findAllByBasicId(@Param("basicId") Integer basicId);

    int findIfManager(@Param("username") String username,
                      @Param("usergh") String usergh);

    ConferenceBasic selectLocalMeetingById(@Param("basicId") Integer basicId);

    List<OrderMsg> selectManagerAll();

    int queryIfExist(@Param("username") String username,
                     @Param("usergh") String usergh);

    int addManager(@Param("username") String username,
                   @Param("usergh") String usergh);

    int deleteManager(@Param("username") String username,
                      @Param("usergh") String usergh);

    List<Integer> queryValidOrInvalidBasicIds(@Param("cstatus") int cstatus);
}
