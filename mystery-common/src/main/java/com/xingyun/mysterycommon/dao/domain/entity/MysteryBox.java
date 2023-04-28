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
 * 盲盒模版表
 * </p>
 *
 * @author xingyun
 * @since 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MysteryBox implements Serializable {

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
     * 购买代币
     */
    private String coin;

    /**
     * 币图标
     */
    private String coinUrl;

    /**
     * 币名称
     */
    private String coinName;

    /**
     * 币精度
     */
    private Integer coinDecimals;

    /**
     * 币符号
     */
    private String coinSymbol;

    /**
     * 价格
     */
    private String price;

    /**
     * 总供应量
     */
    private Integer totalSupply;

    /**
     * 库存
     */
    private Integer stocks;

    /**
     * 销售收入
     */
    private String income;

    /**
     * 价值
     */
    private BigDecimal worth;

    /**
     * 返奖率
     */
    private BigDecimal worthPercent;

    /**
     * 发行方地址
     */
    private String ownerAddress;

    /**
     * 是否审核通过
     */
    private Boolean checkedFlag;

    /**
     * 是否停售
     */
    private Boolean stopFlag;

    /**
     * 是否冻结
     */
    private Boolean frozenFlag;

    /**
     * 创建时间
     */
    private Date createTime;


}
