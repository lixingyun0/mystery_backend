/**
 * peace convince nominee
 * congress useless tobacco
 * dilemma vague task
 * apple error confluence
 * toss nation nose
 */
package com.xingyun.mysterycommon.exception;


/**
 * 锁异常类
 */
public class LockException extends RuntimeException {

    private String errCode;
    private String errMessage;


    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Object... args) {
        super(String.format(message, args));
    }

    public LockException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
