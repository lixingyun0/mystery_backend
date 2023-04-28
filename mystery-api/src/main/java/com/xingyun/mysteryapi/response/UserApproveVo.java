package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserApproveVo {

    /**
     * 钱包地址
     */
    @ApiModelProperty("钱包地址")
    private String userAddress;

    /**
     * 链
     */
    @ApiModelProperty("链")
    private String chain;

    /**
     * 币合约
     */
    @ApiModelProperty("币合约")
    private String tokenContract;

    /**
     * 币图标
     */
    @ApiModelProperty("币图标")
    private String tokenIconUrl;

    /**
     * 币符号
     */
    @ApiModelProperty("币符号")
    private String tokenSymbol;

    /**
     * 精度
     */
    @ApiModelProperty("精度")
    private Integer tokenDecimals;

    /**
     * 授权值
     */
    @ApiModelProperty("授权值")
    private String value;
}
