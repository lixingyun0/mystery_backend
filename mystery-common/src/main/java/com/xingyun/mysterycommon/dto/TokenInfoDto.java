package com.xingyun.mysterycommon.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenInfoDto {

    private String name;

    private String symbol;

    private Integer decimals;

    private String iconUrl;

    private BigDecimal currentPrice;
}
