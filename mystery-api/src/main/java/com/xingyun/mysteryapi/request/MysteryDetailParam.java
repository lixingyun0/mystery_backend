package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MysteryDetailParam {

    @ApiModelProperty("盲盒ID")
    @NotNull
    private String boxId;
}
