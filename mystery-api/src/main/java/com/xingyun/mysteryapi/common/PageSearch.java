package com.xingyun.mysteryapi.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class PageSearch {

    @ApiModelProperty("页码")
    @Min(1)
    public Integer pageIndex = 1;

    @ApiModelProperty("条数")
    @Min(1)
    @Max(20)
    public Integer pageSize = 10;

    /**
     * 构建分页参数
     *
     * @return
     */
    public <T> Page<T> buildPage() {
        return PageDTO.of(this.pageIndex, this.pageSize);
    }

}
