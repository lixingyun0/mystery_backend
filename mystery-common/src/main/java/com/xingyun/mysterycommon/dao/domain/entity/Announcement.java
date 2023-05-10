package com.xingyun.mysterycommon.dao.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公告
 * </p>
 *
 * @author xingyun
 * @since 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否置顶
     */
    private Boolean top;

    /**
     * 是否显示
     */
    private Boolean displayed;

    /**
     * 创建时间
     */
    private Date createTime;


}
