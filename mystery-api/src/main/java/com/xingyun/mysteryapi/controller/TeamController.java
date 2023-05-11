package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.response.TeamVo;
import com.xingyun.mysteryapi.response.WaitClaimAmountVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@Api(tags = "团队")
@RequestMapping("team")
public class TeamController {

    @Autowired
    private IUserAccountService userAccountService;


    @PostMapping("info")
    @Permission
    @ApiOperation("挖矿队伍信息")
    public R<TeamVo> info(){
        TeamVo teamVo = new TeamVo();
        teamVo.setMyTeamPower(new BigDecimal("2323525363.00"));
        teamVo.setMyTeamName("mining dog");
        teamVo.setMyTeamMemberNum(1000L);
        teamVo.setPowerFromMyTeam(teamVo.getMyTeamPower().multiply(new BigDecimal("0.10"))
                .setScale(2, RoundingMode.HALF_DOWN));

        teamVo.setJoinTeamPower(new BigDecimal("67435234523.00"));
        teamVo.setJoinTeamName("tiger");
        teamVo.setJoinTeamMemberNum(996L);
        teamVo.setPowerFromJoinTeam(teamVo.getJoinTeamPower().multiply(new BigDecimal("0.01"))
                .setScale(2,RoundingMode.HALF_DOWN));

        return R.ok(teamVo);
    }



}
