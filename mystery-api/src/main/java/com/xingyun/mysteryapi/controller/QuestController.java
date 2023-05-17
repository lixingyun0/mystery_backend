package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.request.ClaimQuestRewardParam;
import com.xingyun.mysteryapi.response.FlowVo;
import com.xingyun.mysteryapi.response.QuestVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.base.ResultCode;
import com.xingyun.mysterycommon.dao.domain.entity.*;
import com.xingyun.mysterycommon.dao.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("quest")
@Api(tags = "活动任务")
public class QuestController {

    @Autowired
    private IAwardConfigService awardConfigService;

    @Autowired
    private IAwardConfigDetailService awardConfigDetailService;

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private IQuestRewardClaimRecordService questRewardClaimRecordService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private LoadTokenLibrary loadTokenLibrary;


    @PostMapping("list")
    @ApiOperation("任务列表")
    @Permission
    public R<List<QuestVo>> listQuest(){
        List<QuestVo> questVos = new ArrayList<>();
        List<AwardConfig> awardConfigList = awardConfigService.lambdaQuery().orderByAsc(AwardConfig::getAmount).list();
        for (AwardConfig awardConfig : awardConfigList) {
            QuestVo questVo = BeanUtil.toBean(awardConfig, QuestVo.class);
            List<AwardConfigDetail> awardConfigDetails = awardConfigDetailService.lambdaQuery().eq(AwardConfigDetail::getAwardConfigId, awardConfig.getId()).list();
            List<QuestVo.QuestAwardVo> questAwardVos = BeanUtil.copyToList(awardConfigDetails, QuestVo.QuestAwardVo.class);
            for (QuestVo.QuestAwardVo questAwardVo : questAwardVos) {
                boolean exists = questRewardClaimRecordService.lambdaQuery().eq(QuestRewardClaimRecord::getWalletAddress, LoginSession.getWalletAddress())
                        .eq(QuestRewardClaimRecord::getRewardId, questAwardVo.getId())
                        .eq(QuestRewardClaimRecord::getDate, LocalDate.now().toString()).exists();
                questAwardVo.setClaimed(exists);
            }
            questVo.setQuestAwardVoList(questAwardVos);
            questVos.add(questVo);
        }

        return R.ok(questVos);
    }

    @PostMapping("flow")
    @ApiOperation("我的当天流水")
    @Permission
    public R<FlowVo> flow(){

        Map<String, TokenLibrary> tokenLibraryMap = loadTokenLibrary.tokenLibraryMap;

        List<MintRecord> mintRecordList = mintRecordService.lambdaQuery()
                .eq(MintRecord::getMintAddress, LoginSession.getWalletAddress())
                .between(MintRecord::getCreateTime,LocalDateTime.of(LocalDate.now(),LocalTime.MIN),LocalDateTime.of(LocalDate.now(),LocalTime.MAX))
                .list();
        BigDecimal flow = BigDecimal.ZERO;
        for (MintRecord mintRecord : mintRecordList) {
            flow = flow.add(new BigDecimal(loadTokenLibrary.getTokenValue(mintRecord.getCostToken(),mintRecord.getCostTokenAmount()))
                    .multiply(tokenLibraryMap.get(mintRecord.getCostToken()).getCurrentPrice())).setScale(2, RoundingMode.HALF_DOWN);
        }
        FlowVo flowVo = new FlowVo();
        flowVo.setAmount(flow);
        return R.ok(flowVo);
    }

    @PostMapping("claim")
    @ApiOperation("领取任务奖励")
    public R claim(@Valid @RequestBody ClaimQuestRewardParam param){

        AwardConfigDetail awardConfigDetail = awardConfigDetailService.getById(param.getId());

        if (awardConfigDetail == null){
            return R.failed(ResultCode.PARA_ERROR);
        }

        AwardConfig awardConfig = awardConfigService.getById(awardConfigDetail.getAwardConfigId());

        if (awardConfig == null){
            return R.failed(ResultCode.PARA_ERROR);
        }
        Map<String, TokenLibrary> tokenLibraryMap = loadTokenLibrary.tokenLibraryMap;

        List<MintRecord> mintRecordList = mintRecordService.lambdaQuery()
                .eq(MintRecord::getMintAddress, LoginSession.getWalletAddress())
                .between(MintRecord::getCreateTime,LocalDateTime.of(LocalDate.now(),LocalTime.MIN),LocalDateTime.of(LocalDate.now(),LocalTime.MAX))
                .list();
        BigDecimal flow = BigDecimal.ZERO;
        for (MintRecord mintRecord : mintRecordList) {
            flow = flow.add(new BigDecimal(loadTokenLibrary.getTokenValue(mintRecord.getCostToken(),mintRecord.getCostTokenAmount()))
                    .multiply(tokenLibraryMap.get(mintRecord.getCostToken()).getCurrentPrice())).setScale(2, RoundingMode.HALF_DOWN);
        }
        if (flow.compareTo(awardConfig.getAmount()) < 0){
            return R.failed("Insufficient cost amount");
        }

        boolean exists = questRewardClaimRecordService.lambdaQuery().eq(QuestRewardClaimRecord::getWalletAddress, LoginSession.getWalletAddress())
                .eq(QuestRewardClaimRecord::getRewardId, param.getId())
                .eq(QuestRewardClaimRecord::getDate, LocalDate.now()).exists();
        if (exists){
            return R.failed("Claimed");
        }
        QuestRewardClaimRecord questRewardClaimRecord = new QuestRewardClaimRecord();
        questRewardClaimRecord.setRewardId(param.getId());
        questRewardClaimRecord.setDate(LocalDate.now().toString());
        questRewardClaimRecord.setWalletAddress(LoginSession.getWalletAddress());
        questRewardClaimRecordService.save(questRewardClaimRecord);
        //奖励类型 1USDT 2算力卡 3其他代币
        if (awardConfigDetail.getAwardType() == 1){
            System.out.println("给用户发U");
        }
        if (awardConfigDetail.getAwardType() == 2){
            userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress,LoginSession.getWalletAddress())
                    .setSql(" power_card_num = power_card_num + " + awardConfigDetail.getAwardAmount().intValue()).update();
        }
        if (awardConfigDetail.getAwardType() == 3){
            userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress,LoginSession.getWalletAddress())
                    .setSql(" token_amount = token_amount + " + awardConfigDetail.getAwardAmount()).update();
        }
        return R.ok();
    }


}
