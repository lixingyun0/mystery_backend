package com.xingyun.mysterycommon.enums;


public enum ItemTypeEnum {

    OTHER(1, "其他投注项"),

    NUMBER(2, "数字投注项"),

    ;

    private Integer code;
    private String desc;

    ItemTypeEnum(Integer code, String desc) {
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
