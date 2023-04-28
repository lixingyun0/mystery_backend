package com.xingyun.mysterycommon.dao.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 盲盒模版奖励表
 * </p>
 *
 * @author xingyun
 * @since 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MysteryBoxReward implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盲盒ID
     */
    private String boxId;

    /**
     * 奖励代币地址
     */
    private String tokenAddress;

    /**
     * 币图标
     */
    private String tokenUrl;

    /**
     * 奖励代币名称
     */
    private String tokenName;

    /**
     * 奖励代币符号
     */
    private String symbol;

    /**
     * 精度
     */
    private Integer decimals;

    /**
     * 中奖概率
     */
    private BigDecimal probability;

    /**
     * 中奖区间最小值
     */
    private Integer minNumber;

    /**
     * 中奖区间最大值
     */
    private Integer maxNumber;

    /**
     * 奖励代币数量
     */
    private String rewardAmount;

    /**
     * 质押量
     */
    private String stakingAmount;

    /**
     * 创建时间
     */
    private Date createTime;


}
