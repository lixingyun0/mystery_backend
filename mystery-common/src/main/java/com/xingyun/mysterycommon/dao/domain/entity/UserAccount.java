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
 * 用户账户
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 余额
     */
    private BigDecimal tokenAmount;

    /**
     * 待领取的币
     */
    private BigDecimal waitClaimToken;

    /**
     * 永久算力
     */
    private BigDecimal permanentPower;

    /**
     * 我的队伍算力加成
     */
    private BigDecimal myTeamPower;

    /**
     * 我加入的队伍算力加成
     */
    private BigDecimal joinedTeamPower;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 邀请钱包地址
     */
    private String inviteAddress;

    /**
     * 是否开启挖矿
     */
    private Boolean miningFlag;

    /**
     * 算力卡数量
     */
    private Integer powerCardNum;

    /**
     * 挖矿开始时间
     */
    private LocalDateTime mingingStartTime;

    /**
     * 挖矿结束时间
     */
    private LocalDateTime miningEndTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
