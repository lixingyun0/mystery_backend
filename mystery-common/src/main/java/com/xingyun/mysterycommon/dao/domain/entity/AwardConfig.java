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
 * 消费奖励配置表
 * </p>
 *
 * @author xingyun
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AwardConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消费金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private Date createTime;


}
