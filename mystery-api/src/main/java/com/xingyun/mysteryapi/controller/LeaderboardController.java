package com.xingyun.mysteryapi.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.component.LoadTokenLibrary;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.request.ClaimQuestRewardParam;
import com.xingyun.mysteryapi.response.FlowVo;
import com.xingyun.mysteryapi.response.LeaderboardVo;
import com.xingyun.mysteryapi.response.QuestVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.*;
import com.xingyun.mysterycommon.dao.service.IAwardConfigDetailService;
import com.xingyun.mysterycommon.dao.service.IAwardConfigService;
import com.xingyun.mysterycommon.dao.service.ILeaderboardService;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("leaderboard")
@Api(tags = "排行榜")
public class LeaderboardController {


    @Autowired
    private ILeaderboardService leaderboardService;


    @PostMapping("list")
    @ApiOperation("排行列表")
    public R<List<LeaderboardVo>> leaderboard(){
        String issue = LocalDate.now().get(WeekFields.ISO.weekBasedYear()) + "" + LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        List<Leaderboard> leaderboardList = leaderboardService.lambdaQuery()
                .eq(Leaderboard::getIssue, issue)
                .orderByDesc(Leaderboard::getAmount).last(" limit 50").list();
        return R.ok(BeanUtil.copyToList(leaderboardList,LeaderboardVo.class));
    }


    public static void main(String[] args) {

        System.out.println(LocalDate.now().get(WeekFields.ISO.weekOfYear()));
        System.out.println(LocalDate.now().get(WeekFields.ISO.weekBasedYear()));
        System.out.println(LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear()));


    }


}
