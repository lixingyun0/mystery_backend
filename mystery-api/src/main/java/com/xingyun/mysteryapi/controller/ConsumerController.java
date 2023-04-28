package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingyun.mysteryapi.common.PageResult;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.request.*;
import com.xingyun.mysteryapi.response.MintRecordVo;
import com.xingyun.mysteryapi.response.UserApproveVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.contract.ERC20;
import com.xingyun.mysterycommon.dao.domain.entity.DefaultToken;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.domain.entity.TokenLibrary;
import com.xingyun.mysterycommon.dao.domain.entity.UserApprove;
import com.xingyun.mysterycommon.dao.service.IDefaultTokenService;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
import com.xingyun.mysterycommon.dao.service.IUserApproveService;
import com.xingyun.mysterycommon.dto.TokenInfoDto;
import com.xingyun.mysterycommon.service.impl.CoinServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Numeric;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "消费者")
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private IUserApproveService userApproveService;

    @Autowired
    private IDefaultTokenService defaultTokenService;

    @Autowired
    private CoinServiceImpl coinService;

    @Autowired
    private PollingTransactionReceiptProcessor pollingTransactionReceiptProcessor;

    @Autowired
    private LoadTokenLibrary loadTokenLibrary;

    private static String CHAIN = "ethereum";

    @Value("${contract.mystery_address}")
    private String mysteryContractAddress;

    //我的购买记录
    @PostMapping("pageMint")
    @ApiOperation("我的购买记录")
    public R<PageResult<MintRecordVo>> pageMint(@Valid @RequestBody PageMintParam param){
        Page<MintRecord> page = mintRecordService.lambdaQuery()
                .eq(MintRecord::getMintAddress, param.getWalletAddress())
                .orderByDesc(MintRecord::getCreateTime)
                .page(param.buildPage());

        PageResult<MintRecordVo> pageResult = new PageResult<>();
        List<MintRecordVo> mysteryVoList = new ArrayList<>();
        pageResult.setTotal(page.getTotal());
        for (MintRecord record : page.getRecords()) {
            MintRecordVo mintRecordVo = BeanUtil.toBean(record,MintRecordVo.class);
            mysteryVoList.add(mintRecordVo);
            TokenLibrary costToken = loadTokenLibrary.tokenLibraryMap.get(mintRecordVo.getCostToken());
            TokenLibrary rewardToken = loadTokenLibrary.tokenLibraryMap.get(mintRecordVo.getRewardToken());
            mintRecordVo.setCostTokenAmount(loadTokenLibrary.getTokenValue(costToken.getAddress(),mintRecordVo.getCostTokenAmount()));
            mintRecordVo.setCostTokenUrl(costToken.getIconUrl());
            mintRecordVo.setRewardAmount(loadTokenLibrary.getTokenValue(rewardToken.getAddress(),mintRecordVo.getRewardAmount()));
            mintRecordVo.setRewardTokenUrl(rewardToken.getIconUrl());
            mintRecordVo.setChain("BSC");
        }
        pageResult.setRecords(mysteryVoList);
        return R.ok(pageResult);
    }

    //获取购买详情
    @PostMapping("mintDetail")
    @ApiOperation("获取购买详情")
    public R<MintRecordVo> mintDetail(@Valid @RequestBody MintDetailParam param){
        MintRecord mintRecord = mintRecordService.lambdaQuery()
                .eq(MintRecord::getRequestId, param.getRequestId()).one();

        MintRecordVo mintRecordVo = BeanUtil.toBean(mintRecord, MintRecordVo.class);
        TokenLibrary costToken = loadTokenLibrary.tokenLibraryMap.get(mintRecordVo.getCostToken());
        TokenLibrary rewardToken = loadTokenLibrary.tokenLibraryMap.get(mintRecordVo.getRewardToken());
        mintRecordVo.setCostTokenAmount(loadTokenLibrary.getTokenValue(costToken.getAddress(),mintRecordVo.getCostTokenAmount()));
        mintRecordVo.setCostTokenUrl(costToken.getIconUrl());
        mintRecordVo.setRewardAmount(loadTokenLibrary.getTokenValue(rewardToken.getAddress(),mintRecordVo.getRewardAmount()));
        mintRecordVo.setRewardTokenUrl(rewardToken.getIconUrl());
        mintRecordVo.setChain("BSC");
        return R.ok(mintRecordVo);
    }

    //获取购买详情
    @PostMapping("tokenApproveList")
    @ApiOperation("代币授权列表")
    public R<List<UserApproveVo>> listTokenApprove(@Valid @RequestBody ListTokenApproveParam param){

        List<UserApprove> userApproveList = userApproveService.lambdaQuery()
                .eq(UserApprove::getUserAddress, param.getWalletAddress().toLowerCase()).list();
        if (userApproveList == null || userApproveList.size() == 0){
            userApproveList = new ArrayList<>();
            List<DefaultToken> defaultTokens = defaultTokenService.lambdaQuery().list();
            for (DefaultToken defaultToken : defaultTokens) {
                UserApprove userApprove = new UserApprove();
                userApprove.setChain("BSC");
                userApprove.setValue("0");
                userApprove.setTokenDecimals(defaultToken.getTokenDecimals());
                userApprove.setTokenContract(defaultToken.getTokenContract());
                userApprove.setTokenName(defaultToken.getTokenName());
                userApprove.setTokenSymbol(defaultToken.getTokenSymbol());
                userApprove.setTokenIconUrl(defaultToken.getTokenIconUrl());
                userApprove.setUserAddress(param.getWalletAddress().toLowerCase());
                userApproveList.add(userApprove);
            }
            userApproveService.saveBatch(userApproveList);
        }
        List<UserApproveVo> userApproveVos = BeanUtil.copyToList(userApproveList, UserApproveVo.class);
        for (UserApproveVo userApproveVo : userApproveVos) {
            userApproveVo.setValue(loadTokenLibrary.getTokenValue(userApproveVo.getTokenContract(),userApproveVo.getValue()));
        }
        return R.ok(userApproveVos);
    }

    @PostMapping("addToken")
    @ApiOperation("添加代币")
    public R addToken(@Valid @RequestBody AddTokenParam param) throws Exception {

        boolean exists = userApproveService.lambdaQuery().eq(UserApprove::getUserAddress, param.getWalletAddress().toLowerCase())
                .eq(UserApprove::getTokenContract, param.getTokenContract()).exists();
        if (exists){
            return R.ok();
        }
        TokenInfoDto tokenInfo = coinService.getTokenInfo(CHAIN, param.getTokenContract());
        UserApprove userApprove = new UserApprove();
        userApprove.setChain("BSC");
        userApprove.setValue("0");
        userApprove.setTokenDecimals(tokenInfo.getDecimals());
        userApprove.setTokenContract(param.getTokenContract().toLowerCase());
        userApprove.setTokenName(tokenInfo.getName());
        userApprove.setTokenSymbol(tokenInfo.getSymbol());
        userApprove.setTokenIconUrl(tokenInfo.getIconUrl());
        userApprove.setUserAddress(param.getWalletAddress().toLowerCase());
        userApproveService.save(userApprove);
        return R.ok();
    }

    @PostMapping("removeToken")
    @ApiOperation("移除代币")
    public R removeToken(@Valid @RequestBody RemoveTokenParam param){

        UserApprove userApprove = userApproveService.lambdaQuery().eq(UserApprove::getUserAddress, param.getWalletAddress().toLowerCase())
                .eq(UserApprove::getTokenContract, param.getTokenContract().toLowerCase()).one();

        if (userApprove != null){
            userApproveService.removeById(userApprove.getId());
        }
        return R.ok();
    }

    @PostMapping("approveSyn")
    @ApiOperation("同步代币授权 (更新/取消授权)")
    public R approveSyn(@Valid @RequestBody ApproveSynParam param) throws TransactionException, IOException {

        //web3j.get
        TransactionReceipt transactionReceipt = pollingTransactionReceiptProcessor.waitForTransactionReceipt(param.getTransactionHash());

        Log log = transactionReceipt.getLogs().get(0);

        BigInteger value = Numeric.toBigInt(log.getData());
        String owner = "0x"+log.getTopics().get(1).substring(26);
        String spender = "0x"+log.getTopics().get(2).substring(26);
        String tokenContract = transactionReceipt.getTo();
        if (!mysteryContractAddress.equalsIgnoreCase(spender)){
            return R.failed("Not Approve to MysterySpace");
        }
        UserApprove userApprove = userApproveService.lambdaQuery().eq(UserApprove::getUserAddress, owner.toLowerCase())
                .eq(UserApprove::getTokenContract, tokenContract.toLowerCase()).one();

        UserApprove update =  new UserApprove();
        update.setId(userApprove.getId());
        update.setValue(value.toString());
        userApproveService.updateById(update);

        return R.ok();
    }

}
