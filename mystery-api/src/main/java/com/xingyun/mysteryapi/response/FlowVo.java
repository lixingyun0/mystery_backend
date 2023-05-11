package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlowVo {

    @ApiModelProperty("消费额")
    private BigDecimal amount;
}
