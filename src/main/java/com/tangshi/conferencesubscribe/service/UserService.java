package com.tangshi.conferencesubscribe.service;

import com.baomidou.mybatisplus.service.IService;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.domain.ConferenceDetail;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService extends IService<ConferenceDetail> {
    Result queryLocaltion();

    Result order(List<OrderMsg> orderMsgList);

    Result queryMeetingAll(List<OrderMsg> orderMsgList);

    Result queryMeetingAllByUser(List<OrderMsg> orderMsgList);

    Result cancel(List<OrderMsg> orderMsgList);

}
