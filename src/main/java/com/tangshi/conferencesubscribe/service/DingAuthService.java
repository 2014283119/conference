package com.tangshi.conferencesubscribe.service;

import com.tangshi.conferencesubscribe.dto.DingAccessTokenDTO;
import org.springframework.stereotype.Service;

@Service
public interface DingAuthService {

    DingAccessTokenDTO accessToken();

}
