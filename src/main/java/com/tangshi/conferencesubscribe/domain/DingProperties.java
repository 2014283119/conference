package com.tangshi.conferencesubscribe.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "ding.conferencesubscribe")
public class DingProperties {

    private String appKey;
    private String appSecret;
    private String corpId;
    private Long agentId;
    public String dingAccessToken;
    public String dingGetUserId;
    public String dingGetUser;
}
