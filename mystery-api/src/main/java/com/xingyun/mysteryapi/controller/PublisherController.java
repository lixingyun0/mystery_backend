package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingyun.mysteryapi.common.PageResult;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.request.PageOwnerMysteryParam;
import com.xingyun.mysteryapi.request.PublisherSalesParam;
import com.xingyun.mysteryapi.response.MintRecordVo;
import com.xingyun.mysteryapi.response.MysteryVo;
import com.xingyun.mysteryapi.response.PubMysteryVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "发布者")
@RequestMapping("publisher")
public class PublisherController {

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private LoadTokenLibrary loadTokenLibrary;

    //我发布的盲盒列表
    @PostMapping("page")
    @ApiOperation("我发布的盲盒列表")
    public R<PageResult<PubMysteryVo>> page(@Valid @RequestBody PageOwnerMysteryParam param){
        Page<MysteryBox> page = mysteryBoxService.lambdaQuery()
                .eq(MysteryBox::getOwnerAddress,param.getOwner())
                .orderByDesc(MysteryBox::getCreateTime)
                .page(param.buildPage());

        PageResult<PubMysteryVo> pageResult = new PageResult<>();
        List<PubMysteryVo> mysteryVoList = new ArrayList<>();
        pageResult.setTotal(page.getTotal());
        for (MysteryBox record : page.getRecords()) {
            PubMysteryVo mysteryVo = BeanUtil.toBean(record,PubMysteryVo.class);
            mysteryVo.setChain("BSC");
            mysteryVo.setPrice(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getPrice()));
            mysteryVo.setIncome(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getIncome()));
            mysteryVoList.add(mysteryVo);
        }
        pageResult.setRecords(mysteryVoList);
        return R.ok(pageResult);
    }

    //我发布的盲盒销售记录
    @PostMapping("sales")
    @ApiOperation("我发布的盲盒销售记录")
    public R<PageResult<MintRecordVo>> pageSales(@Valid @RequestBody PublisherSalesParam param){
        Page<MintRecord> page = mintRecordService.lambdaQuery()
                .eq(MintRecord::getBoxId, param.getBoxId())
                .orderByDesc(MintRecord::getCreateTime)
                .page(param.buildPage());

        PageResult<MintRecordVo> pageResult = new PageResult<>();
        List<MintRecordVo> mysteryVoList = new ArrayList<>();
        pageResult.setTotal(page.getTotal());
        for (MintRecord record : page.getRecords()) {
            MintRecordVo mintRecordVo = BeanUtil.toBean(record,MintRecordVo.class);
            mintRecordVo.setRewardAmount(loadTokenLibrary.getTokenValue(mintRecordVo.getRewardToken(),mintRecordVo.getRewardAmount()));
            mintRecordVo.setCostTokenAmount(loadTokenLibrary.getTokenValue(mintRecordVo.getCostToken(),mintRecordVo.getCostTokenAmount()));
            mysteryVoList.add(mintRecordVo);
        }
        pageResult.setRecords(mysteryVoList);
        return R.ok(pageResult);
    }
}
