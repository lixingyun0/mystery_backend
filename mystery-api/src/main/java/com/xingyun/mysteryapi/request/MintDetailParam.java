package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MintDetailParam {

    @ApiModelProperty("购买请求ID")
    @NotNull
    private String requestId;
}
