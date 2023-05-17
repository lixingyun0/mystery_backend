package com.xingyun.mysteryjob.job;

import com.xingyun.mysterycommon.dao.domain.entity.BlockMinerRecord;
import com.xingyun.mysterycommon.dao.domain.entity.Team;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.IBlockMinerRecordService;
import com.xingyun.mysterycommon.dao.service.ITeamService;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 挖矿出块定时任务
 */
@Component
public class TeamPowerJob {

    private static final Logger logger = LoggerFactory.getLogger(TeamPowerJob.class);

    @Autowired
    private ITeamService teamService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 10 minutes
     */
    @Scheduled(fixedRate = 600000)
    @Transactional
    public void teamPower(){
        List<Team> teamList = teamService.list();

        //计算队伍总算力，更新队长和队员团队算力加成
        BigDecimal totalPower =  BigDecimal.ZERO;
        for (Team team : teamList) {
            UserAccount leader = userAccountService.lambdaQuery().eq(UserAccount::getWalletAddress, team.getLeader()).one();
            List<UserAccount> userAccountList = userAccountService.lambdaQuery().eq(UserAccount::getInviteAddress, leader)
                    .gt(UserAccount::getMiningEndTime,new Date()).list();

            BigDecimal teamPower =  leader.getPermanentPower().add(userAccountList.stream().map(UserAccount::getPermanentPower)
                    .reduce(BigDecimal.ZERO,BigDecimal::add));

            Team teamUpdate = new Team();
            teamUpdate.setId(team.getId());
            teamUpdate.setTotalPower(teamPower);
            teamService.updateById(teamUpdate);

            userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress,leader.getWalletAddress())
                    .setSql("my_team_power = " + teamPower.multiply(new BigDecimal("0.1")).setScale(0,RoundingMode.HALF_DOWN))
                    .update();

            userAccountService.lambdaUpdate().in(UserAccount::getWalletAddress,userAccountList.stream().map(UserAccount::getWalletAddress).collect(Collectors.toList()))
                    .setSql("joined_team_power = " + teamPower.multiply(new BigDecimal("0.1")).setScale(0,RoundingMode.HALF_DOWN))
                    .update();

            totalPower = totalPower.add(teamPower);
            String cardPower = redisTemplate.opsForValue().get("power_card_" + team.getLeader());
            if (StringUtils.isNotBlank(cardPower)){
                totalPower = totalPower.add(new BigDecimal(cardPower));
            }

        }

        redisTemplate.opsForValue().set("network_power",totalPower.toPlainString());

    }

}
