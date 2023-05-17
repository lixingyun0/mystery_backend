package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.request.AuditParam;
import com.xingyun.mysteryapi.request.MiningParam;
import com.xingyun.mysteryapi.response.MiningVo;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "挖矿")
@RequestMapping("mining")
public class MiningController {

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("info")
    @Permission
    @ApiOperation("挖矿信息")
    public R<MiningVo> info(){
        MiningVo miningVo = new MiningVo();
        UserAccount userAccount = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress()).one();
        miningVo.setPowerCardNum(userAccount.getPowerCardNum());
        String blockNumber = redisTemplate.opsForValue().get("block_number");
        String networkPower = redisTemplate.opsForValue().get("network_power");

        miningVo.setMyPermanentPower(userAccount.getPermanentPower());
        String cardPower = redisTemplate.opsForValue().get("power_card_" + LoginSession.getWalletAddress());

        miningVo.setCardPower(new BigDecimal(cardPower).setScale(0,RoundingMode.HALF_DOWN));
        miningVo.setTotalPower(new BigDecimal(networkPower).setScale(0, RoundingMode.HALF_DOWN));
        miningVo.setBlockNumber(blockNumber);
        miningVo.setMyTeamPower(userAccount.getMyTeamPower());
        miningVo.setJoinedTeamPower(userAccount.getJoinedTeamPower());
        miningVo.setMyTotalPower(miningVo.getMyPermanentPower().add(miningVo.getCardPower()).add(miningVo.getMyTeamPower()).add(miningVo.getJoinedTeamPower()));
        miningVo.setEstimateIncome(miningVo.getMyTotalPower().divide(miningVo.getTotalPower(),6,RoundingMode.HALF_DOWN)
                .multiply(new BigDecimal(86400000)).setScale(0,RoundingMode.HALF_DOWN));
        if (miningVo.getEstimateIncome().compareTo(BigDecimal.ZERO) == 0){
            miningVo.setEstimateIncome(BigDecimal.ONE);
        }
        miningVo.setMiningSessionEndTime(userAccount.getMingingStartTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        miningVo.setMiningSessionStartTime(userAccount.getMiningEndTime().plusHours(24).toInstant(ZoneOffset.UTC).toEpochMilli());
        return R.ok(miningVo);
    }

    @PostMapping("open")
    @Permission
    @ApiOperation("开始挖矿")
    public R open(@Valid @RequestBody MiningParam param){
        UserAccount userAccount = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress()).one();

        Integer powerCardNum = param.getPowerCardNum();
        if (powerCardNum > userAccount.getPowerCardNum()){
            return R.failed("Insufficient power card num");
        }
        UserAccount update = new UserAccount();
        update.setId(userAccount.getId());
        update.setMiningFlag(true);
        update.setMingingStartTime(LocalDateTime.now());
        update.setMiningEndTime(update.getMingingStartTime().plusHours(24));
        update.setPowerCardNum(userAccount.getPowerCardNum() - powerCardNum);
        userAccountService.updateById(update);

        redisTemplate.opsForValue().set("power_card_"+LoginSession.getWalletAddress(),String.valueOf(1000*powerCardNum),24, TimeUnit.HOURS);
        return R.ok();
    }

    @PostMapping("claim")
    @Permission
    @ApiOperation("领取挖矿奖励")
    public R claim(){
        userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress())

                .setSql(" token_amount = token_amount + wait_claim_token, wait_claim_token = 0").update();

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
