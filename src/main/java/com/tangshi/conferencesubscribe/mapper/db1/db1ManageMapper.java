package com.tangshi.conferencesubscribe.mapper.db1;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tangshi.conferencesubscribe.anno.db1Dao;
import com.tangshi.conferencesubscribe.domain.ConferenceBasic;
import com.tangshi.conferencesubscribe.domain.LocaltionMeetingName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@db1Dao
@Mapper
public interface db1ManageMapper extends BaseMapper<ConferenceBasic> {
    List<LocaltionMeetingName> queryLocaltion();

    Integer selectIfexist(@Param("localtionName") String localtionName);

    Integer insertLocaltion(@Param("localtionName") String localtionName);

    Integer selectMeetingIfexist(@Param("localtionName") String localtionName,
                                 @Param("meetingName") String meetingName);

    Integer selectIdByLocaltion(@Param("localtionName") String localtionName);

    Integer selectMeetingIfexistById(@Param("id") Integer id,
                                     @Param("meetingName") String meetingName);
}
