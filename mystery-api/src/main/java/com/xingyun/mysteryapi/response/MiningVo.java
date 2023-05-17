package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MiningVo {

    @ApiModelProperty("我的可用算力卡数量")
    private Integer powerCardNum;

    @ApiModelProperty("我的永久算力")
    private BigDecimal myPermanentPower;

    @ApiModelProperty("当前算力卡算力")
    private BigDecimal cardPower;

    /**
     * 我的队伍算力加成
     */
    @ApiModelProperty("我的队伍算力加成")
    private BigDecimal myTeamPower;

    /**
     * 我加入的队伍算力加成
     */
    @ApiModelProperty("我加入的队伍算力加成")
    private BigDecimal joinedTeamPower;

    @ApiModelProperty("我的总算力")
    private BigDecimal myTotalPower;

    @ApiModelProperty("全网算力")
    private BigDecimal totalPower;

    @ApiModelProperty("当前区块数")
    private String blockNumber;

    @ApiModelProperty("挖矿结束时间")
    private Long miningSessionEndTime;

    @ApiModelProperty("挖矿开始时间")
    private Long miningSessionStartTime;

    @ApiModelProperty("预估收益(每天)")
    private BigDecimal estimateIncome;

}
