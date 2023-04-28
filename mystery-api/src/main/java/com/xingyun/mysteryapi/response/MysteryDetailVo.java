package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MysteryDetailVo {

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
    @ApiModelProperty("发布交易ID")
    private String transactionHash;

    /**
     * 购买代币
     */
    @ApiModelProperty("购买使用代币")
    private String coin;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private String price;

    /**
     * 总供应量
     */
    @ApiModelProperty("总供应量")
    private Integer totalSupply;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stocks;

    /**
     * 销售收入
     */
    @ApiModelProperty("销售收入")
    private String income;

    /**
     * 发行方地址
     */
    @ApiModelProperty("发行方地址")
    private String ownerAddress;

    /**
     * 是否停售
     */
    @ApiModelProperty("是否停售")
    private Boolean stopFlag;

    /**
     * 是否冻结
     */
    @ApiModelProperty("是否冻结")
    private Boolean frozenFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long createTime;

    /**
     * 币图标
     */
    @ApiModelProperty("币图标")
    private String coinUrl;

    /**
     * 币名称
     */
    @ApiModelProperty("币名称")
    private String coinName;

    /**
     * 币精度
     */
    @ApiModelProperty("币精度")
    private Integer coinDecimals;

    /**
     * 币符号
     */
    @ApiModelProperty("币符号")
    private String coinSymbol;

    /**
     * 返奖率
     */
    @ApiModelProperty("返奖率")
    private BigDecimal worthPercent;

    /**
     * 是否审核通过
     */
    @ApiModelProperty("是否审核通过")
    private Boolean checkedFlag;

    @ApiModelProperty("奖品列表")
    List<MysteryBoxRewardVo> rewardList;
}
