package com.xingyun.mysterycommon.dao.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 任务奖励领取记录
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestRewardClaimRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 日期
     */
    private String date;

    /**
     * 奖励ID
     */
    private Long rewardId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
