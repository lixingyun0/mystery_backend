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
 * 消费奖励配置详情表
 * </p>
 *
 * @author xingyun
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AwardConfigDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消费奖励配置ID
     */
    private Long awardConfigId;

    /**
     * 奖励类型 1USDT 2算力卡 3其他代币
     */
    private Integer awardType;

    /**
     * 奖励数量
     */
    private BigDecimal awardAmount;

    /**
     * 奖励名称
     */
    private String awardName;

    /**
     * 创建时间
     */
    private Date createTime;


}
