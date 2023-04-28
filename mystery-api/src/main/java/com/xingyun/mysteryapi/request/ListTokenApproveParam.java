package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ListTokenApproveParam {

    @ApiModelProperty("钱包地址")
    @NotBlank
    private String walletAddress;
}
