package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DepositListener {

    private Logger logger = LoggerFactory.getLogger(DepositListener.class);

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    @Autowired
    private MysterySpaceChainLink contract;

    @Autowired
    private RedissonClient redissonClient;

    public DepositListener(){
        System.out.println("Init DepositListener");
    }

    @RabbitListener(queues = {"Deposit"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        logger.info("DepositListener:{}",origin);

        String[] split = origin.split("@");
        String txHash = split[0];
        String data = split[1];
        BigInteger boxId = Numeric.toBigInt(data);

        RLock lock = redissonClient.getLock("LockKey:MysteryBox:"+boxId);

        try {
            boolean tryLock = lock.tryLock(10, 20, TimeUnit.SECONDS);
            if (!tryLock) {
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            List<MysteryBoxReward> mysteryBoxRewards = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, boxId).list();

            for (MysteryBoxReward mysteryBoxReward : mysteryBoxRewards) {

                try {
                    BigInteger amount = contract.getBoxAccountInfo(boxId, mysteryBoxReward.getTokenAddress()).send();
                    if (!mysteryBoxReward.getStakingAmount().equals(amount.toString())){
                        MysteryBoxReward update = new MysteryBoxReward();
                        update.setId(mysteryBoxReward.getId());
                        update.setStakingAmount(amount.toString());
                        mysteryBoxRewardService.updateById(update);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
