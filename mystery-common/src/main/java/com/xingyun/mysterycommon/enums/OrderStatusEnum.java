package com.xingyun.mysterycommon.enums;


public enum OrderStatusEnum {

    ORDERED(0, "未开奖"),

    SUCCESS(1, "已开奖"),

    CANCEL(2, "已取消"),
    ;


    private Integer code;
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
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
