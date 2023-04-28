package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.request.AuditParam;
import com.xingyun.mysteryapi.request.CoinParam;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import com.xingyun.mysterycommon.dto.TokenInfoDto;
import com.xingyun.mysterycommon.service.impl.CoinServiceImpl;
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
@Api(tags = "币相关")
@RequestMapping("coin")
public class CoinController {

    private static String CHAIN = "ethereum";

    @Autowired
    private CoinServiceImpl coinService;

    @PostMapping("info")
    @ApiOperation("根据合约地址获取币信息")
    public R<TokenInfoDto> coinInfo(@Valid @RequestBody CoinParam param) throws Exception {
        TokenInfoDto tokenInfo = coinService.getTokenInfo(CHAIN, param.getCoinContract());
        return R.ok(tokenInfo);
    }


}
