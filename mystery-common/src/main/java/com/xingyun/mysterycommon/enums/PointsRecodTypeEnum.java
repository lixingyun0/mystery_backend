package com.xingyun.mysterycommon.enums;


public enum PointsRecodTypeEnum {

    //1上分 2下分 3投注奖励 4邀请投注奖励 5投注消耗 6投注赢取

    RECHARGE(1, "上分"),
    WITHDRAW(2, "下分"),
    LOTTERY_REWARD(3, "投注返水"),
    INVITE_REWARD(4, "邀请投注返水"),
    CONSUME(5, "投注消耗"),
    WIN(6, "投注赢取"),
    TRANSFER_FROM(7, "转出"),
    TRANSFER_TO(8, "转入"),


    ;


    private Integer code;
    private String desc;

    PointsRecodTypeEnum(Integer code, String desc) {
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
