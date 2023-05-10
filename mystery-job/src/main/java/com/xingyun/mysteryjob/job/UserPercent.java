package com.xingyun.mysteryjob.job;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPercent {

    private String walletAddress;

    private BigDecimal percent;
}
