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
 * 代币库
 * </p>
 *
 * @author xingyun
 * @since 2023-04-25
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
    private Date createTime;


}
