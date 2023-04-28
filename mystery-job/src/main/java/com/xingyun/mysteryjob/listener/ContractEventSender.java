package com.xingyun.mysteryjob.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ContractEventSender {

    private Logger logger = LoggerFactory.getLogger(ContractEventSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEvent(String event,String msg) {
        logger.info("contract event send: {},{}",event,msg);
        rabbitTemplate.send(event,new Message(msg.getBytes(StandardCharsets.UTF_8)));

    }
}
