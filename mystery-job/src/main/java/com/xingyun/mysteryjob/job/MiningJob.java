package com.xingyun.mysteryjob.job;

import com.xingyun.mysterycommon.dao.domain.entity.BlockMinerRecord;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.IBlockMinerRecordService;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 挖矿出块定时任务
 */
@Component
public class MiningJob {

    private static final Logger logger = LoggerFactory.getLogger(MiningJob.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IBlockMinerRecordService blockMinerRecordService;

    @Scheduled(fixedDelay = 7000)
    public void mining(){

        Long blockNumber = redisTemplate.opsForValue().increment("block_number");

        List<UserAccount> userAccountList = userAccountService.lambdaQuery().eq(UserAccount::getMiningFlag, true).list();

        BigDecimal totalPower = userAccountList.stream().map(userAccount ->
                userAccount.getPermanentPower().add(userAccount.getMyTeamPower()).add(userAccount.getJoinedTeamPower()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<UserPercent> userPercentList = new ArrayList<>();

        BigDecimal start = BigDecimal.ZERO;
        for (UserAccount userAccount : userAccountList) {
            start = userAccount.getPermanentPower().add(userAccount.getMyTeamPower())
                    .add(userAccount.getJoinedTeamPower()).divide(totalPower,8, RoundingMode.HALF_DOWN).add(start);
            UserPercent userPercent = new UserPercent();
            userPercent.setWalletAddress(userPercent.getWalletAddress());
            userPercent.setPercent(new BigDecimal(start.toString()));
            userPercentList.add(userPercent);
        }
        Random random = new Random();
        double v = random.nextDouble();
        for (UserPercent userPercent : userPercentList) {
            if (BigDecimal.valueOf(v).compareTo(userPercent.getPercent()) <=0){
                logger.info("{}获得{}的记账权，奖励代币",userPercent.getWalletAddress(),blockNumber);

                BlockMinerRecord blockMinerRecord = new BlockMinerRecord();
                blockMinerRecord.setMinerAddress(userPercent.getWalletAddress());
                blockMinerRecord.setBlockNumber(blockNumber);
                blockMinerRecord.setReward(new BigDecimal("7"));
                blockMinerRecordService.save(blockMinerRecord);

                userAccountService.lambdaUpdate()
                        .eq(UserAccount::getWalletAddress,userPercent.getWalletAddress())
                        .setSql(" wait_claim = wait_claim + " + blockMinerRecord.getReward()).update();
                return;
            }
        }
    }

    public static void main(String[] args) {

        List<BigDecimal> bigDecimals = new ArrayList<>();
        bigDecimals.add(new BigDecimal("10023"));
        bigDecimals.add(new BigDecimal("987945"));
        bigDecimals.add(new BigDecimal("1435"));
        bigDecimals.add(new BigDecimal("78754"));
        bigDecimals.add(new BigDecimal("3498794"));
        bigDecimals.add(new BigDecimal("234234"));
        bigDecimals.add(new BigDecimal("6567"));
        bigDecimals.add(new BigDecimal("134356"));
        bigDecimals.add(new BigDecimal("9889985"));
        bigDecimals.add(new BigDecimal("453534"));
        bigDecimals.add(new BigDecimal("12245"));
        bigDecimals.add(new BigDecimal("7777"));
        bigDecimals.add(new BigDecimal("8888"));
        bigDecimals.add(new BigDecimal("9999"));
        bigDecimals.add(new BigDecimal("99999"));

        BigDecimal start = BigDecimal.ZERO;
        List<BigDecimal> percentList = new ArrayList<>();
        BigDecimal totalPower = bigDecimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        for (BigDecimal bigDecimal : bigDecimals) {
            start = bigDecimal.divide(totalPower,8, RoundingMode.HALF_DOWN).add(start);
            percentList.add(new BigDecimal(start.toString()));
        }

        for (int j = 0; j < 100; j++) {
            Random random = new Random();
            double v = random.nextDouble();

            int i = 0;
            for (BigDecimal percent : percentList) {
                if (BigDecimal.valueOf(v).compareTo(percent) <=0){
                    System.out.println("============");
                    System.out.println(i);
                    System.out.println(v);
                    System.out.println("============");
                    break;
                }
                i++;
            }
        }



    }
}
