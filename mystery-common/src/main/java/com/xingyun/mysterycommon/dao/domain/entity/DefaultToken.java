package com.xingyun.mysterycommon.dao.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 默认币
 * </p>
 *
 * @author xingyun
 * @since 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 链
     */
    private String chain;

    /**
     * 币合约
     */
    private String tokenContract;

    /**
     * 币名称
     */
    private String tokenName;

    /**
     * 币图标
     */
    private String tokenIconUrl;

    /**
     * 币符号
     */
    private String tokenSymbol;

    /**
     * 精度
     */
    private Integer tokenDecimals;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
