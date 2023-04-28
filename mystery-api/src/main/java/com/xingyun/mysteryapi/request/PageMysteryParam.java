package com.xingyun.mysteryapi.request;

import com.xingyun.mysteryapi.common.PageSearch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PageMysteryParam extends PageSearch {

    @ApiModelProperty(value = "链",example = "BSC,ETHEREUM")
    @NotBlank
    private String chain;

    @ApiModelProperty("排序 0默认 1返奖率 2售价高 3售价低 4最新发布")
    @NotNull
    private Integer sort;
}
