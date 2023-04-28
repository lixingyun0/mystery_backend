package com.xingyun.mysteryapi.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuditParam {

    @ApiModelProperty("盲盒ID")
    @NotBlank
    private String boxId;
}
