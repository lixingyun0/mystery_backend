package com.xingyun.mysterycommon.exception;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误明细
     */
    private String detailMessage;

    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Object... args) {
        super(String.format(message, args));
    }

    public BizException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable throwable) {
        super(message, throwable);
    }

}