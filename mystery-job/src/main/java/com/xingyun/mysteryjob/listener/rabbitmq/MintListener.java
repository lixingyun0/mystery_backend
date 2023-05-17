package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.*;
import com.xingyun.mysterycommon.dao.service.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.concurrent.TimeUnit;

@Component
public class MintListener {

    private Logger logger = LoggerFactory.getLogger(MintListener.class);

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private ITokenLibraryService tokenLibraryService;

    @Autowired
    private IUserAccountService userAccountService;


    @Autowired
    private ILeaderboardService leaderboardService;


    @Autowired
    private RedissonClient redissonClient;

    public MintListener(){
        System.out.println("Init MintListener");
    }

    @RabbitListener(queues = {"Mint"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        logger.info("MintListener:{}",origin);

        String[] split = origin.split("@");
        String txHash = split[0];
        String data = split[1];
        BigInteger boxId = Numeric.toBigInt(data.substring(0,66));
        RLock lock = redissonClient.getLock("LockKey:MysteryBox:"+boxId);

        try {
            boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS);
            if (!tryLock) {
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            MintRecord mintRecord = mintRecordService.lambdaQuery().eq(MintRecord::getTransactionHash, txHash).one();
            if (mintRecord != null){
                return;
            }
            BigInteger requestId = Numeric.toBigInt(data.substring(66,130));
            String address =  "0x"+ data.substring(154);

            MysteryBox mysteryBox = mysteryBoxService.lambdaQuery().eq(MysteryBox::getBoxId, boxId).one();

            MysteryBox mysteryBoxUpdate = new MysteryBox();
            mysteryBoxUpdate.setId(mysteryBox.getId());
            mysteryBoxUpdate.setStocks(mysteryBox.getStocks() -1);
            mysteryBoxUpdate.setIncome(new BigInteger(mysteryBox.getIncome()).add(new BigInteger(mysteryBox.getPrice())).toString());
            mysteryBoxService.updateById(mysteryBoxUpdate);

            mintRecord = new MintRecord();
            mintRecord.setMintAddress(address.toLowerCase());
            mintRecord.setTransactionHash(txHash);
            mintRecord.setBoxId(boxId.toString());
            mintRecord.setClaimed(false);
            mintRecord.setFulfilled(false);
            mintRecord.setRequestId(requestId.toString());
            mintRecord.setCostToken(mysteryBox.getCoin());
            mintRecord.setCostTokenAmount(mysteryBox.getPrice());
            mintRecord.setCostTokenSymbol(mysteryBox.getCoinSymbol());

            mintRecordService.save(mintRecord);

            TokenLibrary tokenLibrary = tokenLibraryService.lambdaQuery().eq(TokenLibrary::getAddress, mysteryBox.getCoin()).one();
            BigDecimal tokenValuePrice = tokenLibrary.getTokenValueUSDT(mysteryBox.getPrice());
            BigDecimal power = tokenValuePrice.multiply(new BigDecimal("1000")).setScale(2, RoundingMode.HALF_DOWN);
            if (power.compareTo(BigDecimal.ZERO) >0 ){
                userAccountService.lambdaUpdate().eq(UserAccount::getWalletAddress,address.toLowerCase())
                        .setSql(" permanent_power = permanent_power + " + power);
            }

            //leaderboard
            String issue = LocalDate.now().get(WeekFields.ISO.weekBasedYear()) + "" + LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());

            Leaderboard leaderboard = leaderboardService.lambdaQuery().eq(Leaderboard::getWalletAddress, address.toLowerCase())
                    .eq(Leaderboard::getIssue, issue).one();

            if (leaderboard == null){
                Leaderboard leaderboardAdd= new Leaderboard();
                leaderboardAdd.setWalletAddress(address.toLowerCase());
                leaderboardAdd.setAmount(tokenValuePrice);
                leaderboardAdd.setIssue(issue);
                leaderboardAdd.setClaimed(false);
                leaderboardAdd.setReward(BigDecimal.ZERO);
                leaderboardService.save(leaderboardAdd);
            }else {
                Leaderboard leaderboardUpdate= new Leaderboard();
                leaderboardUpdate.setId(leaderboard.getId());
                leaderboardUpdate.setAmount(leaderboard.getAmount().add(tokenValuePrice));

                leaderboardService.updateById(leaderboardUpdate);
            }


        }catch (RuntimeException e) {
            throw e;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

}
