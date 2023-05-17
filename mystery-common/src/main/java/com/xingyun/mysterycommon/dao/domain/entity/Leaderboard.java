package com.xingyun.mysterycommon.dao.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 排行榜
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Leaderboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 期数
     */
    private String issue;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 奖励
     */
    private BigDecimal reward;

    /**
     * 是否领取
     */
    private Boolean claimed;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
