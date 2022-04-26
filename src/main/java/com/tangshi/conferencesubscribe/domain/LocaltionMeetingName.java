package com.tangshi.conferencesubscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocaltionMeetingName{
    private Integer id;
    private String localtion;
    private String meetingName;
    private Integer cstatus;
    private Integer maximumPeople;
}
