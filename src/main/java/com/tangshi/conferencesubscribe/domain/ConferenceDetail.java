package com.tangshi.conferencesubscribe.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("conference_detail")
public class ConferenceDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer basicId;
    private String username;
    private String usergh;
    private String yyyt;
    private Date createTime;
    private Date beginTime;
    private Date endTime;
    private String ifexist; //逻辑删除标记 0否1是 默认为1
    private String uuid;
    private String temporalInterval;
}
