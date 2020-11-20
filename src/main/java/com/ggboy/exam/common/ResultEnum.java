package com.ggboy.exam.common;

/**
 * 601:请求成功
 * 701:请求失败
 * 702:未登录
 * 703:权限不足
 * 704:请求参数错误
 * 705:
 */
public enum ResultEnum {
    SUCCESS(601,"请求成功"),
    LOGIN_FAIL(710,"登录失败"),
    LOGIN_EXPIRE(711,"登录过期"),
    FAIL(701,"请求失败"),
    USERNAME_ERROR(705,"用户名不存在"),
    PASSWORD_ERROR(706,"密码错误"),
    NO_LOGIN(702,"未登录"),
    NO_AUTHORITY(703,"权限不足"),
    PARAM_ERROR(704,"请求参数错误"),
    USERNAME_REPEAT(707,"用户名重复"),
    TELEPHONE_REPEAT(708,"该手机已经注册"),
    EMAIL_REPEAT(709,"该邮箱已经注册"),
    SYSTEM_ERROR(500,"系统错误"),;


    private int code;

    private String message;

    ResultEnum(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
