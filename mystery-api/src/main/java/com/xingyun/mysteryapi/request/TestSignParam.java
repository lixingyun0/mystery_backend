package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TestSignParam {

    @ApiModelProperty("原始文本")
    private String originText;

    @ApiModelProperty("私钥")
    private String privateKey;
}
