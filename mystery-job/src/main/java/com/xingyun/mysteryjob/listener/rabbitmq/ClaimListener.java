package com.xingyun.mysteryjob.listener.rabbitmq;

import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
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
public class ClaimListener {

    private Logger logger = LoggerFactory.getLogger(ClaimListener.class);

    @Autowired
    private IMintRecordService mintRecordService;

    @Autowired
    private CheckTransactionHelper checkTransactionHelper;

    public ClaimListener(){
        System.out.println("Init ClaimListener");
    }

    @RabbitListener(queues = {"Claim"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        logger.info("ClaimListener:{}",origin);

        String[] split = origin.split("@");
        String txHash = split[0];
        boolean pass = checkTransactionHelper.checkTxHash(txHash, "Claim");
        if (!pass){
            return;
        }
        String data = split[1];
        BigInteger requestId = Numeric.toBigInt(data);

        MintRecord mintRecord = mintRecordService.lambdaQuery().eq(MintRecord::getRequestId, requestId).one();
        if (mintRecord.getClaimed()){
            return;
        }
        MintRecord update = new MintRecord();
        update.setId(mintRecord.getId());
        update.setClaimed(true);
        mintRecordService.updateById(update);
    }

}
