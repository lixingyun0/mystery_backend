package com.xingyun.mysteryapi.component;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRes {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("钱包地址")
    private String walletAddress;
}
