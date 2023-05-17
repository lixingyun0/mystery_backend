package com.xingyun.mysterycommon.dao.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 代币库
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TokenLibrary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 奖励代币地址
     */
    private String address;

    /**
     * 奖励代币名称
     */
    private String name;

    /**
     * 奖励代币符号
     */
    private String symbol;

    /**
     * 精度
     */
    private Integer decimals;

    /**
     * 图标地址
     */
    private String iconUrl;

    /**
     * 最新价格
     */
    private BigDecimal currentPrice;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public BigDecimal getTokenValue(String amount){
        return new BigDecimal(amount).divide(new BigDecimal(Math.pow(10,this.getDecimals())),4, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getTokenValueUSDT(String amount){
        return getTokenValue(amount).multiply(this.getCurrentPrice()).setScale(4,RoundingMode.HALF_DOWN);
    }


}
