package com.tangshi.common.handle;

import com.tangshi.common.contants.ResultCodeEnum;
import com.tangshi.common.exception.CustomException;
import com.tangshi.common.vo.Result;
import com.tangshi.common.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof CustomException) {   //判断异常是否是我们定义的异常
            CustomException customException = (CustomException) e;
            return ResultUtil.error(ResultCodeEnum.CUSTOM_EXCEPTION_ERROR);
        }else{
            logger.error(
                "[系统异常]"
            , e);
            return ResultUtil.error(ResultCodeEnum.EXCEPTION_ERROR);
        }
    }
}