package com.tangshi.conferencesubscribe.service;

import com.tangshi.conferencesubscribe.dto.DingUserDTO;
import com.tangshi.conferencesubscribe.dto.DingUserIdDTO;
import org.springframework.stereotype.Service;

@Service
public interface DingUserService {

    DingUserIdDTO getUserId(String access_token, String code);

    DingUserDTO getUserInfo(String access_token, String userid);
}
