package com.ggboy.exam.common;

import java.io.Serializable;

/**
 * 成功或者失败
 *
 * @param <T>
 */
public class ResultResponse<T> implements Serializable {
    //100---请求失败
    //200---成功
    //300---参数错误

    private int code;

    private String message;

    private T data;

    private ResultResponse(int code){
        this.code=code;
    }
    private ResultResponse(int code,String message){
        this(code);

        this.message=message;
    }
    private ResultResponse(int code,String message,T data){
        this(code,message);
        this.data = data;
    }

    /**
     * 成功不携带数据
     * @return
     */
    public static ResultResponse success(){
        return new ResultResponse(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 成功并且携带数据
     * @param t
     * @param <T>
     * @return
     */
    public static<T> ResultResponse success(T t){
        ResultResponse success = success();
        success.setData(t);
        return success;
    }

    /**
     * 失败不携带数据 不携带信息
     * 请求失败
     * @param <T>
     * @return
     */
    public static<T> ResultResponse fail(){
        return new ResultResponse(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMessage());
    }

    /**
     * 其它失败 不携带数据
     * @param resultEnum
     * @param <T>
     * @return
     */
    public static<T> ResultResponse fail(ResultEnum resultEnum){
        return new ResultResponse(resultEnum.getCode(),resultEnum.getMessage());
    }

    public static<T> ResultResponse fail(String message){
        return new ResultResponse(ResultEnum.LOGIN_FAIL.getCode(),message);
    }
    /**
     * 失败 携带数据
     * @param resultEnum
     * @param <T>
     * @return
     */
    public static<T> ResultResponse fail(ResultEnum resultEnum, T data){
        ResultResponse fail = fail(resultEnum);
        fail.setData(data);
        return fail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public boolean isSuccess(){
        return this.code==ResultEnum.SUCCESS.getCode();
    }

}
