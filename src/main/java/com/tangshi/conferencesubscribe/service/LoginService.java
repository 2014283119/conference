package com.tangshi.conferencesubscribe.service;

import com.tangshi.common.vo.Result;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    Result verifyUserId(String userId);

    Result findAll();
}
