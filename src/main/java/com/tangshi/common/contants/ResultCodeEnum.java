package com.tangshi.common.contants;
/**
 * .::::.
 * .::::::::.
 * :::::::::::    佛主保佑、永无Bug
 * ..:::::::::::'
 * '::::::::::::'
 * .::::::::::
 * '::::::::::::::..
 * ..::::::::::::.
 * ``::::::::::::::::
 * ::::``:::::::::'        .:::.
 * ::::'   ':::::'       .::::::::.
 * .::::'      ::::     .:::::::'::::.
 * .:::'       :::::  .:::::::::' ':::::.
 * .::'        :::::.:::::::::'      ':::::.
 * .::'         ::::::::::::::'         ``::::.
 * ...:::           ::::::::::::'              ``::.
 * ```` ':.          ':::::::::'                  ::::..
 * '.:::::'                    ':'````..
 */


import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    //0 ->成功
    //2开头 系统错误
    //4开头 前端参数错误
    SUCCESS(0,"成功"),
    TRUE(1,"存在"),
    FALSE(0,"不存在"),
    LOGIN_FAIL(-1,"身份信息校验不通过,登陆失败"),
    DB1_ERROR(20001,"系统出错,请联系管理员"),
    DB2_ERROR(20002,"系统出错,请联系管理员"),
    CUSTOM_EXCEPTION_ERROR(30001,"系统出错,请联系管理员"),
    EXCEPTION_ERROR(30002,"系统出错,请联系管理员"),
    PARAMETER_LACK_FAIL(40001,"系统出错,请联系管理员"),//前端未做非空校验,参数为空报错
    ENDTIME_LT_BEGINTIME_ERROR(40002,"预约开始时间不能大于结束时间"),//前端未做校验,预约开始时间大于结束时间报错
    TIME_CONFLICT_ERROR(50001,"这条订单刚刚被预定了"),
    CONFERENCE_BASIC_FAIL(60001,"该会议室已存在"),
    USER_INFO_EXIST_FAIL(60002,"该用户已经是管理员");//用户名与工号已存在

    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
