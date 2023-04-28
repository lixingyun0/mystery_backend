package com.xingyun.lotteryadmin.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginVo {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("账号")
    private String account;
}
