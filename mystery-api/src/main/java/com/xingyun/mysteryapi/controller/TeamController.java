package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.response.TeamVo;
import com.xingyun.mysteryapi.response.WaitClaimAmountVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.Team;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.ITeamService;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private ITeamService teamService;

    @PostMapping("info")
    @Permission
    @ApiOperation("挖矿队伍信息")
    public R<TeamVo> info(){

        UserAccount userAccount = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, LoginSession.getWalletAddress()).one();
        Team myTeam = teamService.lambdaQuery().eq(Team::getLeader, LoginSession.getWalletAddress()).one();
        TeamVo teamVo = new TeamVo();
        teamVo.setMyTeamLeader(LoginSession.getWalletAddress());
        teamVo.setMyTeamPower(myTeam.getTotalPower());
        teamVo.setMyTeamName(myTeam.getTeamName());
        teamVo.setMyTeamMemberNum(myTeam.getMemberNum());
        teamVo.setPowerFromMyTeam(userAccount.getMyTeamPower());

        if (StringUtils.isNotBlank(userAccount.getInviteAddress())){
            Team joinTeam = teamService.lambdaQuery().eq(Team::getLeader, userAccount.getInviteAddress()).one();
            teamVo.setJoinTeamLeader(userAccount.getInviteAddress());
            teamVo.setJoinTeamPower(joinTeam.getTotalPower());
            teamVo.setJoinTeamName(joinTeam.getTeamName());
            teamVo.setJoinTeamMemberNum(joinTeam.getMemberNum());
            teamVo.setPowerFromJoinTeam(userAccount.getJoinedTeamPower());
        }else {
            teamVo.setJoinTeamLeader("");
            teamVo.setJoinTeamPower(BigDecimal.ZERO);
            teamVo.setJoinTeamName("");
            teamVo.setJoinTeamMemberNum(0L);
            teamVo.setPowerFromJoinTeam(BigDecimal.ZERO);
        }
        return R.ok(teamVo);
    }



}
