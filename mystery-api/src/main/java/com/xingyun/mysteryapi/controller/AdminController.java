package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.request.AuditParam;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "管理员")
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    @Autowired
    private MysterySpaceChainLink contract;

    public R audit(@Valid @RequestBody AuditParam param){
        MysteryBox mysteryBox = mysteryBoxService.lambdaQuery().eq(MysteryBox::getBoxId, param.getBoxId()).one();

        if (mysteryBox.getCheckedFlag()){
            return R.ok();
        }

        List<MysteryBoxReward> mysteryBoxRewardList = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, param.getBoxId()).list();

        return R.ok();
    }


}
