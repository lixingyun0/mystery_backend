package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginWithWalletParam {

    @NotBlank
    @Length(min = 30,max = 50)
    @ApiModelProperty(value = "address",required = true)
    private String address;

    @NotBlank
    @ApiModelProperty(value = "signature",required = true)
    private String signature;

    @NotBlank
    @ApiModelProperty(value = "origin text to sign",required = true)
    private String originText;
}
