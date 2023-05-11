package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MiningVo {

    @ApiModelProperty("我的算力")
    private BigDecimal myPower;

    @ApiModelProperty("全网算力")
    private BigDecimal totalPower;

    @ApiModelProperty("当前区块数")
    private Long blockNumber;

    @ApiModelProperty("挖矿结束时间")
    private Long miningSessionEndTime;

    @ApiModelProperty("预估收益(每小时)")
    private BigDecimal estimateIncome;

}
