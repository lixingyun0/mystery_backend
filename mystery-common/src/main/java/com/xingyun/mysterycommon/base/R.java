package com.xingyun.mysterycommon.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private static final Object EMPTY = new Object();
    /**
     * 返回标记：成功标记 0，失败标记=1
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public R() {

    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static R ok() {
        return ok(EMPTY);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(ResultCode.SUCCESS.code, ResultCode.SUCCESS.msg, data);
    }


    public static R failed() {
        return failed("failed");
    }

    public static R failed(String msg) {
        return failed(ResultCode.FAIL.code, msg);
    }


    public static R failed(int code, String msg) {
        return failed(EMPTY, code, msg);
    }

    public static R failed(ResultCode resultCode) {
        return failed(EMPTY, resultCode.code, resultCode.msg);
    }

    public static <T> R<T> failed(T data, int code, String msg) {
        return new R<>(code, msg, data);
    }


    public Boolean isSuccess() {
        return code == 0;
    }
}

