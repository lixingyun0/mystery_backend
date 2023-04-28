package com.xingyun.mysterycommon.enums;

public enum WithdrawStatusEnum {

    CHECKING(0, "审核中"),

    SUCCESS(1, "已处理"),

    DENY(2, "拒绝"),


    ;

    private Integer code;
    private String desc;

    WithdrawStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
