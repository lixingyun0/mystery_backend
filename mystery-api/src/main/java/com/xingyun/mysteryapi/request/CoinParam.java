package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CoinParam {

    @ApiModelProperty("币合约地址")
    private String coinContract;
}
