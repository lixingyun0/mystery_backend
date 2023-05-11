package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestVo {

    @ApiModelProperty("任务ID")
    private String id;

    @ApiModelProperty("消费额")
    private BigDecimal amount;

    @ApiModelProperty("消费奖励列表")
    private List<QuestAwardVo> questAwardVoList;

    @Data
    public static class QuestAwardVo{

        @ApiModelProperty("奖励ID")
        private String id;

        @ApiModelProperty("奖励类型 1USDT 2算力卡 3其他代币")
        /**
         * 奖励类型 1USDT 2算力卡 3其他代币
         */
        private Integer awardType;

        @ApiModelProperty("奖励数量")
        /**
         * 奖励数量
         */
        private BigDecimal awardAmount;

        @ApiModelProperty("奖励名称")
        /**
         * 奖励名称
         */
        private String awardName;
    }


}
