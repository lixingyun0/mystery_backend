package com.xingyun.mysteryapi.request;

import com.xingyun.mysteryapi.common.PageSearch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PublisherSalesParam extends PageSearch {

    @ApiModelProperty("盲盒ID")
    @NotNull
    private Long boxId;
}
