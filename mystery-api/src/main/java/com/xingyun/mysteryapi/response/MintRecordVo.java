package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MintRecordVo {

    @ApiModelProperty(value = "链",example = "BSC")
    private String chain;

    /**
     * 盲盒ID
     */
    @ApiModelProperty("盲盒ID")
    private String boxId;

    /**
     * 交易ID
     */
    @ApiModelProperty("交易ID")
    private String transactionHash;

    /**
     * 购买地址
     */
    @ApiModelProperty("购买地址")
    private String mintAddress;

    /**
     * chainlink请求ID
     */
    @ApiModelProperty("chainlink请求ID")
    private String requestId;

    /**
     * 随机数是否回填
     */
    @ApiModelProperty("随机数是否回填")
    private Boolean fulfilled;

    /**
     * chainlink随机词
     */
    @ApiModelProperty("chainlink随机词")
    private String randomWord;

    /**
     * 时间戳
     */
    @ApiModelProperty("时间戳")
    private Long timestamp;

    /**
     * 随机数
     */
    @ApiModelProperty("随机数")
    private Long randomNumber;

    /**
     * 奖励token
     */
    @ApiModelProperty("奖励token")
    private String rewardToken;

    /**
     * 奖励数量
     */
    @ApiModelProperty("奖励数量")
    private String rewardAmount;

    /**
     * 币符号
     */
    @ApiModelProperty("奖励token符号")
    private String rewardTokenSymbol;

    @ApiModelProperty("奖励币图标")
    private String rewardTokenUrl;

    /**
     * 消费币
     */
    @ApiModelProperty("消费token")
    private String costToken;

    /**
     * 消费币符号
     */
    @ApiModelProperty("消费token符号")
    private String costTokenSymbol;

    /**
     * 消费币数量
     */
    @ApiModelProperty("消费数量")
    private String costTokenAmount;

    @ApiModelProperty("消费币图标")
    private String costTokenUrl;

    /**
     * 是否领取奖励
     */
    @ApiModelProperty("是否领取奖励")
    private Boolean claimed;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

}
