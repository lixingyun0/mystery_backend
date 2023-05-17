package com.xingyun.mysterycommon.dao.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 购买盲盒记录表
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MintRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盲盒ID
     */
    private String boxId;

    /**
     * 交易ID
     */
    private String transactionHash;

    /**
     * 购买地址
     */
    private String mintAddress;

    /**
     * chainlink请求ID
     */
    private String requestId;

    /**
     * 消费币
     */
    private String costToken;

    /**
     * 消费币符号
     */
    private String costTokenSymbol;

    /**
     * 消费币数量
     */
    private String costTokenAmount;

    /**
     * 随机数是否回填
     */
    private Boolean fulfilled;

    /**
     * chainlink随机词
     */
    private String randomWord;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 随机数
     */
    private Long randomNumber;

    /**
     * 奖励token
     */
    private String rewardToken;

    /**
     * 币符号
     */
    private String rewardTokenSymbol;

    /**
     * 奖励数量
     */
    private String rewardAmount;

    /**
     * 是否领取奖励
     */
    private Boolean claimed;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
