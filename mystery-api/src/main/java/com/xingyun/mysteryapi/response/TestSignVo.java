package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TestSignVo {

    @ApiModelProperty("sign1 (ethereum标准)")
    private String sign1;

    @ApiModelProperty("sign2")
    private String sign2;
}
