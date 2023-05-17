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
 * 团队
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队长
     */
    private String leader;

    /**
     * 队名
     */
    private String teamName;

    /**
     * 队员数量
     */
    private Long memberNum;

    /**
     * 团队总算力
     */
    private BigDecimal totalPower;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
