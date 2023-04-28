package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import com.xingyun.mysteryjob.component.CheckTransactionHelper;
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

@Component
public class PublisherClaimListener {

    private Logger logger = LoggerFactory.getLogger(PublisherClaimListener.class);

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private CheckTransactionHelper checkTransactionHelper;

    public PublisherClaimListener(){
        System.out.println("Init PublisherClaimListener");
    }

    @RabbitListener(queues = {"PublisherClaim"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        logger.info("PublisherClaimListener:{}",origin);

        String[] split = origin.split("@");
        String txHash = split[0];
        boolean pass = checkTransactionHelper.checkTxHash(txHash, "PublisherClaim");
        if (!pass){
            return;
        }
        String data = split[1];

        BigInteger boxId = Numeric.toBigInt(data);

        MysteryBox mysteryBox = mysteryBoxService.lambdaQuery().eq(MysteryBox::getBoxId, boxId).one();
        MysteryBox update = new MysteryBox();
        update.setId(mysteryBox.getId());
        update.setIncome("0");
        mysteryBoxService.updateById(update);
    }

}
