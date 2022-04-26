package com.tangshi.common.util;

import com.tangshi.common.contants.ResultCodeEnum;
import com.tangshi.common.vo.Result;

public class ResultUtil {

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    public static Result error(ResultCodeEnum resultCodeEnum) {
        Result result = new Result();
        result.setCode(resultCodeEnum.getCode());
        result.setMsg(resultCodeEnum.getMessage());
        return result;
    }

    public static Result fail(ResultCodeEnum resultCodeEnum) {
        Result result = new Result();
        result.setCode(resultCodeEnum.getCode());
        result.setMsg(resultCodeEnum.getMessage());
        return result;
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}