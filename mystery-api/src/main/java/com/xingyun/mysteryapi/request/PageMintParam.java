package com.xingyun.mysteryapi.request;

import com.xingyun.mysteryapi.common.PageSearch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PageMintParam extends PageSearch {

    @ApiModelProperty("钱包地址")
    @NotBlank
    private String walletAddress;
}
