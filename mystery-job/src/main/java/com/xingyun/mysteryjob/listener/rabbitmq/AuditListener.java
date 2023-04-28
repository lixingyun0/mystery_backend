package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
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
public class AuditListener {

    private Logger logger = LoggerFactory.getLogger(AuditListener.class);

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    public AuditListener(){
        System.out.println("Init AuditListener");
    }

    @RabbitListener(queues = {"Audit"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("AuditListener:{}",origin);
        String[] split = origin.split("@");
        String txHash = split[0];
        String data = split[1];
        BigInteger boxId = Numeric.toBigInt(data);

        MysteryBox mysteryBox = mysteryBoxService.lambdaQuery().eq(MysteryBox::getBoxId, boxId).one();
        if (mysteryBox == null){
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (mysteryBox.getCheckedFlag()){
            return;
        }
        MysteryBox update = new MysteryBox();
        update.setId(mysteryBox.getId());
        update.setCheckedFlag(true);
        mysteryBoxService.updateById(update);
    }

}
