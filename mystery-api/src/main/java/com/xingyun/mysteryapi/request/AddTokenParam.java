package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddTokenParam {

    @ApiModelProperty("钱包地址")
    @NotBlank
    private String walletAddress;

    @ApiModelProperty("代币合约地址")
    @NotBlank
    private String tokenContract;
}
