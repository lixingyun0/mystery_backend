package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingyun.mysteryapi.common.PageResult;
import com.xingyun.mysteryapi.common.PageSearch;
import com.xingyun.mysteryapi.response.AnnouncementVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.Announcement;
import com.xingyun.mysterycommon.dao.service.IAnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("announcement")
@Api(tags = "公告")
public class AnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    @PostMapping("pageAnnouncement")
    @ApiOperation("公告列表")
    @Transactional(readOnly = true)
    public R<PageResult<AnnouncementVo>> pageAnnouncement(@Valid @RequestBody PageSearch pageSearch) {
        Page<Announcement> page = announcementService.lambdaQuery()
                .eq(Announcement::getDisplayed,true)
                .orderByDesc(Announcement::getCreateTime).page(pageSearch.buildPage());
        PageResult<AnnouncementVo> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(BeanUtil.copyToList(page.getRecords(), AnnouncementVo.class));
        return R.ok(pageResult);
    }

    @PostMapping("topAnnouncement")
    @ApiOperation("获取置顶公告")
    @Transactional(readOnly = true)
    public R<AnnouncementVo> topAnnouncement() {
        Announcement announcement = announcementService.lambdaQuery()
                .eq(Announcement::getDisplayed,true)
                .eq(Announcement::getTop, true).one();

        if (announcement == null) {
            announcement = announcementService.lambdaQuery()
                    .eq(Announcement::getDisplayed,true)
                    .orderByDesc(Announcement::getCreateTime).last(" limit 1").one();
        }

        return R.ok(BeanUtil.toBean(announcement, AnnouncementVo.class));
    }
}
