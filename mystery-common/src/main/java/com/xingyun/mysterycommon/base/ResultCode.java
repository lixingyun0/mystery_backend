package com.xingyun.mysterycommon.base;

public enum ResultCode {
    SUCCESS("成功", 0),
    BIZ_ERROR("业务错误", -1),
    PARA_ERROR("参数错误", -2),
    FAIL("系统繁忙", -3),
    KEY_REPEAT("记录重复", -4),
    UN_EXIST("记录不存在", -5),
    UN_LOGIN("未登录", -6),
    NOT_AUTH("操作权限不足", -8),
    REPEAT_LOGIN("重复登录", -9),
    INVALID_IP("非法IP", -10),
    OPERATE_FREQUENTLY("操作频繁,请稍后再试", -10),
    ;

    public int code;

    public String msg;

    private ResultCode(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
