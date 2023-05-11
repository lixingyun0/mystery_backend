package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClaimQuestRewardParam {

    @ApiModelProperty("奖励ID")
    @NotNull
    private Long id;
}
