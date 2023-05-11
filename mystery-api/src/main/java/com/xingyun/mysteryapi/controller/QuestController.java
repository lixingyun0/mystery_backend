package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.request.ClaimQuestRewardParam;
import com.xingyun.mysteryapi.response.FlowVo;
import com.xingyun.mysteryapi.response.QuestVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.AwardConfig;
import com.xingyun.mysterycommon.dao.domain.entity.AwardConfigDetail;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.domain.entity.TokenLibrary;
import com.xingyun.mysterycommon.dao.service.IAwardConfigDetailService;
import com.xingyun.mysterycommon.dao.service.IAwardConfigService;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
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
    private LoadTokenLibrary loadTokenLibrary;


    @PostMapping("list")
    @ApiOperation("任务列表")
    public R<List<QuestVo>> listQuest(){
        List<QuestVo> questVos = new ArrayList<>();
        List<AwardConfig> awardConfigList = awardConfigService.lambdaQuery().orderByAsc(AwardConfig::getAmount).list();
        for (AwardConfig awardConfig : awardConfigList) {
            QuestVo questVo = BeanUtil.toBean(awardConfig, QuestVo.class);
            List<AwardConfigDetail> awardConfigDetails = awardConfigDetailService.lambdaQuery().eq(AwardConfigDetail::getAwardConfigId, awardConfig.getId()).list();
            questVo.setQuestAwardVoList(BeanUtil.copyToList(awardConfigDetails, QuestVo.QuestAwardVo.class));
            questVos.add(questVo);
        }

        return R.ok(questVos);
    }

    @PostMapping("flow")
    @ApiOperation("我的当天流水")
    @Permission
    public R<FlowVo> flow(){

        Map<String, TokenLibrary> tokenLibraryMap = loadTokenLibrary.tokenLibraryMap;

        List<MintRecord> mintRecordList = mintRecordService.lambdaQuery().eq(MintRecord::getMintAddress, LoginSession.getWalletAddress()).list();
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

        return R.ok();
    }
}
