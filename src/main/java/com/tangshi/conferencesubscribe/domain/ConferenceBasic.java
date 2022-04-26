package com.tangshi.conferencesubscribe.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("conference_basic")
public class ConferenceBasic {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer localtionId;
    private String localtionName;
    private String meetingName;
    private Integer maximumPeople;
    private Integer cstatus;
    private Integer ifexist;
}
