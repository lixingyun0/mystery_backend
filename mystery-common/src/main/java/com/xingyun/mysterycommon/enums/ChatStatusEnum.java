package com.xingyun.mysterycommon.enums;


public enum ChatStatusEnum {

    NO_REPLY(0, "未回复"),

    REPLY(1, "已回复"),

    NONE(2, "无需回复"),
    ;


    private Integer code;
    private String desc;

    ChatStatusEnum(Integer code, String desc) {
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
