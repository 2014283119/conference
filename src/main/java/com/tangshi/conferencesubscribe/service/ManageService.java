package com.tangshi.conferencesubscribe.service;

import com.baomidou.mybatisplus.service.IService;
import com.tangshi.common.vo.Result;
import com.tangshi.conferencesubscribe.domain.ConferenceBasic;
import com.tangshi.conferencesubscribe.domain.OrderMsg;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManageService extends IService<ConferenceBasic> {

    Result queryMeetingAll(List<OrderMsg> list);

    Result deactivate(List<OrderMsg> list);

    Result enable(List<OrderMsg> list);

    Result queryLocaltion();

    Result delete(List<OrderMsg> list);

    Result insertx(List<ConferenceBasic> list);

    Result modify(List<ConferenceBasic> list);

    Result queryManagerAll();

    Result addManager(List<OrderMsg> list);

    Result deleteManager(List<OrderMsg> list);
}
