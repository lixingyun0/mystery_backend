package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MiningParam {

    @ApiModelProperty("算力卡数量")
    private Integer powerCardNum;
}
