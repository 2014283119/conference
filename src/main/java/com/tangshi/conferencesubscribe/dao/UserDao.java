package com.tangshi.conferencesubscribe.dao;

import com.tangshi.conferencesubscribe.domain.LocaltionMeetingName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDao {
    private final static Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private com.tangshi.conferencesubscribe.mapper.db1.db1Mapper db1Mapper;

    public List<LocaltionMeetingName> queryLocaltion(){
        //TODO
        List<LocaltionMeetingName> localMeetingNames = null;
        try{

            localMeetingNames = db1Mapper.queryLocaltion();
        }catch (Exception e){
            logger.error("===UserDao.queryLocaltion() localMeetingNames:"+localMeetingNames+"===",e);
        }
        return localMeetingNames;
    }

    public int queryBasicId(String localtion_name, String meeting_name) {
        int basicId = -1;
        try{
            basicId = db1Mapper.queryBasicId(localtion_name, meeting_name);
        }catch (Exception e){
            logger.error("===UserDao.queryBasicId() basicId:"+basicId+"===",e);
        }
        return basicId;
    }
}
