package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Component
public class RequestFulfilledListener {

    private Logger logger = LoggerFactory.getLogger(RequestFulfilledListener.class);

    @Autowired
    private MysterySpaceChainLink contract;

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    public RequestFulfilledListener(){
        System.out.println("Init RequestFulfilledListener");
    }

    //这里就要算出具体奖励
    @RabbitListener(queues = {"RequestFulfilled"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        logger.info("RequestFulfilledListener:{}",origin);

        String[] split = origin.split("@");
        String txHash = split[0];
        String data = split[1];

        BigInteger requestId = Numeric.toBigInt(data);
        MintRecord mintRecord = mintRecordService.lambdaQuery().eq(MintRecord::getRequestId, requestId).one();
        if (mintRecord.getFulfilled()){
            return;
        }
        try {
            Tuple8<String, BigInteger, BigInteger, BigInteger, BigInteger, Boolean, Boolean, Boolean> requestStatus = contract.requestsMapping(requestId).send();
            MintRecord update = new MintRecord();
            update.setId(mintRecord.getId());
            update.setFulfilled(requestStatus.component6());
            update.setRandomWord(requestStatus.component3().toString());
            update.setTimestamp(requestStatus.component5().longValue());
            update.setRandomNumber(requestStatus.component4().longValue());
            MysteryBoxReward mysteryBoxReward = mysteryBoxRewardService.lambdaQuery().eq(MysteryBoxReward::getBoxId, mintRecord.getBoxId())
                    .le(MysteryBoxReward::getMinNumber, update.getRandomNumber())
                    .ge(MysteryBoxReward::getMaxNumber, update.getRandomNumber()).one();
            update.setRewardToken(mysteryBoxReward.getTokenAddress());
            update.setRewardAmount(mysteryBoxReward.getRewardAmount());
            update.setRewardTokenSymbol(mysteryBoxReward.getSymbol());
            mintRecordService.updateById(update);

            MysteryBoxReward mysteryBoxRewardUpdate = new MysteryBoxReward();
            mysteryBoxRewardUpdate.setId(mysteryBoxReward.getId());
            mysteryBoxRewardUpdate.setStakingAmount(new BigInteger(mysteryBoxReward.getStakingAmount()).subtract(new BigInteger(mysteryBoxReward.getRewardAmount())).toString());
            mysteryBoxRewardService.updateById(mysteryBoxRewardUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
