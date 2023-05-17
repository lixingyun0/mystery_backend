package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeaderboardVo {

    /**
     * 钱包地址
     */
    @ApiModelProperty("钱包地址")
    private String walletAddress;

    /**
     * 期数
     */
    @ApiModelProperty("期数")
    private String issue;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal amount;

    /**
     * 奖励
     */
    @ApiModelProperty("奖励")
    private BigDecimal reward;

    /**
     * 是否领取
     */
    @ApiModelProperty("是否领取")
    private Boolean claimed;
}
