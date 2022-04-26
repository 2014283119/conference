package com.tangshi.common.exception;

import com.tangshi.common.contants.ResultCodeEnum;

public class CustomException extends RuntimeException{    //注意：spring中，只有RuntimeException才会进行事务回滚，Exception不会进行事务回滚

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "DPRException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}