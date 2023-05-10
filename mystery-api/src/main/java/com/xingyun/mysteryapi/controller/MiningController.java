package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.request.AuditParam;
import com.xingyun.mysteryapi.response.WaitClaimAmountVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "挖矿")
@RequestMapping("mining")
public class MiningController {

    @Autowired
    private IUserAccountService userAccountService;

    @PostMapping("open")
    @Permission
    @ApiOperation("开始挖矿")
    public R open(){
        UserAccount userAccount = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress()).one();

        UserAccount update = new UserAccount();
        update.setId(userAccount.getId());
        update.setMiningFlag(true);
        userAccountService.updateById(update);
        return R.ok();
    }

    @PostMapping("claim")
    @Permission
    @ApiOperation("领取挖矿奖励")
    public R claim(){
        userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress())

                .setSql(" token_amount = token_amount + wait_claim, wait_claim = 0").update();

        return R.ok();
    }

    @PostMapping("waitClaimAmount")
    @Permission
    @ApiOperation("待领取奖励")
    public R<WaitClaimAmountVo> waitClaimAmount(){
        UserAccount userAccount = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress()).one();
        WaitClaimAmountVo waitClaimAmountVo = new WaitClaimAmountVo();
        waitClaimAmountVo.setWaitClaimAmount(userAccount.getWaitClaimToken());
        return R.ok(waitClaimAmountVo);
    }


}
