package com.xingyun.mysteryapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnouncementVo {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

}
