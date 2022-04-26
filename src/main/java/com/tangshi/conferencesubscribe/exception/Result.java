package com.tangshi.conferencesubscribe.exception;

import org.slf4j.MDC;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = 1888932078692392845L;
    /**
     * 标识接口调用是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String msg = "";

    /**
     * 请求id，用来追踪
     */
    private String requestId = "";

    /**
     * 符合条件的记录
     */
    private T data;

    public Result() {
        success = false;
        code = "";
        msg = "";
    }

    public Result(boolean success) {
        this.requestId = MDC.get("traceId");
        this.success = success;
        code = "";
        msg = "";
    }

    public Result(T data) {
        this.requestId = MDC.get("traceId");
        success = true;
        this.data = data;
        code = "";
        msg = "";
    }

    public Result(String code, String msg) {
        this.requestId = MDC.get("traceId");
        success = false;
        this.code = code;
        this.msg = msg;
    }

    public Result(boolean success, String code, String msg) {
        this.requestId = MDC.get("traceId");
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public Result(boolean success, String code, String msg, T data) {
        this.requestId = MDC.get("traceId");
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> succeed() {
        return new Result(true, BizExceptionEnums.SUCCEED_CODE.getCode(), BizExceptionEnums.SUCCEED_CODE.getMsg());
    }

    /**
     * 返回成功结果
     *
     * @param data 返回的数据
     * @param <T>
     * @return
     */
    public static <T> Result<T> succeed(T data) {
        return new Result<T>(true, BizExceptionEnums.SUCCEED_CODE.getCode(), BizExceptionEnums.SUCCEED_CODE.getMsg(), data);
    }

    /**
     * 返回成功结果
     *
     * @param field 指定节点名称
     * @param data  返回的数据
     * @param <T>
     * @return
     */
    public static <T> Result succeed(String field, T data) {
        Map<String, T> params = new HashMap<String, T>(1);
        params.put(field, data);
        return new Result(params);
    }

    public static <T> Result<T> succeed(String code, String msg, T data) {
        return new Result<T>(true, code, msg, data);
    }

    /**
     * 返回失败结果
     *
     * @param code 失败场景的代码
     * @param msg  失败场景的消息
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String code, String msg) {
        return new Result<T>(code, msg);
    }


    /**
     * 返回失败结果
     *
     * @return
     */
    public static <T> Result<T> fail(String code) {
        return fail(code, null);
    }

    /**
     * 失败使用  BIZ_FAILED_CODE("21000", "业务处理失败"),作为异常
     */
    public static <T> Result<T> fail() {
        return fail(BizExceptionEnums.BIZ_FAILED_CODE.getCode(), BizExceptionEnums.BIZ_FAILED_CODE.getMsg());
    }

    public static <T> Result<T> fail(String code, String msg, T data) {
        return new Result<T>(false, code, msg, data);
    }

    /**
     * get,set属性
     */
    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(String code) {
        this.code = code;
    }
}