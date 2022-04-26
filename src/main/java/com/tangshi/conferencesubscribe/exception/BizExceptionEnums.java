package com.tangshi.conferencesubscribe.exception;

/**
 * code可以作为统一的应答码
 * @author zhengjt
 * @date 20220324
 */
public enum BizExceptionEnums {
    SUCCEED_CODE("00000", "处理成功"),
    FAILED_CODE("00001", "系统异常"),
    NO_ACCOUNT_CODE("00003", "账户不存在"),
    MISSING_PARAM_CODE("10000", "参数为空"),
    ILLEGAL_PARAM_CODE("10001", "参数非法"),
    NOT_SUPPORTED_FILE_CODE("10002", "不支持的文件格式"),
    NO_AUTHORITY_CODE("11000", "无权限操作"),
    BIZ_FAILED_CODE("21000", "业务处理失败"),
    BIZ_PROCESSING_CODE("21001", "业务处理中"),
    MISS_TARGET_CODE("20000", "需操作的对象不存在"),
    REPEAT_TARGET_CODE("20001", "需操作的对象已经存在"),
    ILLEGAL_OPERATE_CODE("20002", "非法的业务操作"),
    REQUEST_TIME_OUT("30001", "调用第三方接口超时");

    private String code;
    private String msg;

    BizExceptionEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static BizExceptionEnums statOf(String code) {
        for (BizExceptionEnums state : values())
            if (state.getCode().equals(code))
                return state;
        return null;
    }
}