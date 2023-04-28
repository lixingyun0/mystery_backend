package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MysteryBoxRewardVo {

    /**
     * 奖励代币地址
     */
    @ApiModelProperty("奖励代币地址")
    private String tokenAddress;

    /**
     * 奖励代币名称
     */
    @ApiModelProperty("奖励代币名称")
    private String tokenName;

    /**
     * 奖励代币符号
     */
    @ApiModelProperty("奖励代币符号")
    private String symbol;

    /**
     * 精度
     */
    @ApiModelProperty("精度")
    private Integer decimals;

    /**
     * 中奖概率
     */
    @ApiModelProperty("中奖概率")
    private BigDecimal probability;

    /**
     * 中奖区间最小值
     */
    @ApiModelProperty("中奖区间最小值")
    private Integer minNumber;

    /**
     * 中奖区间最大值
     */
    @ApiModelProperty("中奖区间最大值")
    private Integer maxNumber;

    /**
     * 奖励代币数量
     */
    @ApiModelProperty("奖励代币数量")
    private String rewardAmount;

    /**
     * 质押量
     */
    @ApiModelProperty("质押量")
    private String stakingAmount;


    /**
     * 币图标
     */
    @ApiModelProperty("币图标")
    private String tokenUrl;

}
