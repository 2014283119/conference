package com.tangshi.conferencesubscribe.exception;

import lombok.Data;

@Data
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -1248773038268188511L;

    private String code;
    private String message;

    public BizException() {

    }

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}