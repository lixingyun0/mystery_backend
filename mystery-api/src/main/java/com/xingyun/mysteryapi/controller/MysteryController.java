package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingyun.mysteryapi.common.PageResult;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.request.MysteryDetailParam;
import com.xingyun.mysteryapi.request.PageMysteryParam;
import com.xingyun.mysteryapi.response.MysteryBoxRewardVo;
import com.xingyun.mysteryapi.response.MysteryDetailVo;
import com.xingyun.mysteryapi.response.MysteryVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
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
import java.util.stream.Collectors;

@RestController
@Api(tags = "盲盒相关")
@RequestMapping("mystery")
public class MysteryController {

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    @Autowired
    private LoadTokenLibrary loadTokenLibrary;

    //盲盒列表
    @PostMapping("page")
    @ApiOperation("盲盒分页列表")
    public R<PageResult<MysteryVo>> page(@Valid  @RequestBody PageMysteryParam param){

        LambdaQueryChainWrapper<MysteryBox> chainWrapper = mysteryBoxService.lambdaQuery()
                .eq(MysteryBox::getCheckedFlag, true)
                .eq(MysteryBox::getStopFlag,false);
        //0默认 1返奖率 2售价高 3售价低 4最新发布
        switch (param.getSort()){
            case 0:{
                chainWrapper.orderByDesc(MysteryBox::getCreateTime);
                break;
            }
            case 1:{
                chainWrapper.orderByDesc(MysteryBox::getWorthPercent);
                break;
            }
            case 2:{
                chainWrapper.orderByDesc(MysteryBox::getPrice);
                break;
            }
            case 3:{
                chainWrapper.orderByAsc(MysteryBox::getPrice);
                break;
            }
            case 4:{
                chainWrapper.orderByDesc(MysteryBox::getCreateTime);
                break;
            }
        }
        Page<MysteryBox> page = chainWrapper.page(param.buildPage());

        PageResult<MysteryVo> pageResult = new PageResult<>();
        List<MysteryVo> mysteryVoList = new ArrayList<>();
        pageResult.setTotal(page.getTotal());
        for (MysteryBox record : page.getRecords()) {
            MysteryVo mysteryVo = BeanUtil.toBean(record,MysteryVo.class);
            List<String> iconList = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, record.getBoxId()).list()
                    .stream().map(MysteryBoxReward::getTokenUrl).collect(Collectors.toList());
            mysteryVo.setPrizeIconList(iconList);
            mysteryVo.setChain("BSC");
            mysteryVo.setPrice(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getPrice()));
            mysteryVo.setIncome(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getIncome()));
            mysteryVoList.add(mysteryVo);
        }
        pageResult.setRecords(mysteryVoList);
        return R.ok(pageResult);
    }

    //盲盒详情
    @PostMapping("detail")
    @ApiOperation("盲盒详情")
    public R<MysteryDetailVo> detail(@Valid  @RequestBody MysteryDetailParam param){
        MysteryBox mysteryBox = mysteryBoxService.lambdaQuery().eq(MysteryBox::getBoxId, param.getBoxId()).one();
        MysteryDetailVo mysteryDetailVo = BeanUtil.toBean(mysteryBox, MysteryDetailVo.class);

        List<MysteryBoxReward> mysteryBoxRewards = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, param.getBoxId()).list();
        List<MysteryBoxRewardVo> mysteryBoxRewardVos = BeanUtil.copyToList(mysteryBoxRewards, MysteryBoxRewardVo.class);
        for (MysteryBoxRewardVo mysteryBoxRewardVo : mysteryBoxRewardVos) {
            mysteryBoxRewardVo.setRewardAmount(loadTokenLibrary.getTokenValue(mysteryBoxRewardVo.getTokenAddress(),mysteryBoxRewardVo.getRewardAmount()));
            mysteryBoxRewardVo.setStakingAmount(loadTokenLibrary.getTokenValue(mysteryBoxRewardVo.getTokenAddress(),mysteryBoxRewardVo.getStakingAmount()));
        }
        mysteryDetailVo.setRewardList(mysteryBoxRewardVos);
        mysteryDetailVo.setChain("BSC");
        mysteryDetailVo.setPrice(loadTokenLibrary.getTokenValue(mysteryDetailVo.getCoin(),mysteryDetailVo.getPrice()));
        mysteryDetailVo.setIncome(loadTokenLibrary.getTokenValue(mysteryDetailVo.getCoin(),mysteryDetailVo.getIncome()));
        return R.ok(mysteryDetailVo);
    }

    //热门盲盒
    @PostMapping("hot")
    @ApiOperation("热门盲盒")
    public R<List<MysteryVo>> hot(){
        List<MysteryBox> mysteryBoxList = mysteryBoxService.lambdaQuery()
                .eq(MysteryBox::getCheckedFlag, true)
                .eq(MysteryBox::getStopFlag,false)
                .orderByDesc(MysteryBox::getCreateTime).last(" limit 2")
                .list();

        List<MysteryVo> mysteryVoList = new ArrayList<>();
        for (MysteryBox mysteryBox : mysteryBoxList) {
            MysteryVo mysteryVo = BeanUtil.toBean(mysteryBox,MysteryVo.class);
            List<String> iconList = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, mysteryBox.getBoxId()).list()
                    .stream().map(MysteryBoxReward::getTokenUrl).collect(Collectors.toList());
            mysteryVo.setPrizeIconList(iconList);
            mysteryVo.setChain("BSC");
            mysteryVo.setPrice(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getPrice()));
            mysteryVo.setIncome(loadTokenLibrary.getTokenValue(mysteryVo.getCoin(),mysteryVo.getIncome()));
            mysteryVoList.add(mysteryVo);
        }
        return R.ok(mysteryVoList);
    }
}
