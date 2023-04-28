package com.xingyun.mysteryjob.listener;

import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


@Component
public class MysteryBoxContractListener {

    public MysteryBoxContractListener(){
        System.out.println("Init MysteryBoxContractListener====");
    }

    private static Logger logger = LoggerFactory.getLogger(MysteryBoxContractListener.class);

    @Autowired
    private Web3j web3;

    @Autowired
    private ContractEventSender contractEventSender;

    @Value("${contract.mystery_address}")
    private String mysteryContractAddress;

    private static final String SEPARATOR = "@";

    @PostConstruct
    public void listen() throws IOException {

        //获取启动时监听的区块
        Request<?, EthBlockNumber> request = web3.ethBlockNumber();
        BigInteger fromblock = request.send().getBlockNumber();

        EthFilter ethFilter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(3370478L)),
                //如果监听不到这里的地址可以把 0x 给去掉
                DefaultBlockParameterName.LATEST, mysteryContractAddress);

        logger.info("Starting listen mystery contract event... ,{}",mysteryContractAddress);

        web3.ethLogFlowable(ethFilter).subscribe(log -> {

            logger.info("contract listen data:{}", log.toString());

            List<String> topics = log.getTopics();
            String topic = topics.get(0);

            if (EventEncoder.encode(MysterySpaceChainLink.PUBLISHMYSTERY_EVENT).equals(topic)){
                //发布盲盒
                contractEventSender.sendEvent(MysterySpaceChainLink.PUBLISHMYSTERY_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.MINT_EVENT).equals(topic)){
                //购买盲盒
                contractEventSender.sendEvent(MysterySpaceChainLink.MINT_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.CLAIM_EVENT).equals(topic)){
                //领取盲盒奖励
                contractEventSender.sendEvent(MysterySpaceChainLink.CLAIM_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.DEPOSIT_EVENT).equals(topic)){
                //发行方补币
                contractEventSender.sendEvent(MysterySpaceChainLink.DEPOSIT_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.STOPSALE_EVENT).equals(topic)){
                //发行方停售
                contractEventSender.sendEvent(MysterySpaceChainLink.STOPSALE_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.PUSHLISHERCLAIM_EVENT).equals(topic)){
                //发行方提取销售额
                contractEventSender.sendEvent(MysterySpaceChainLink.PUSHLISHERCLAIM_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }
            if (EventEncoder.encode(MysterySpaceChainLink.AUDIT_EVENT).equals(topic)){
                //管理员审核盲盒
                contractEventSender.sendEvent(MysterySpaceChainLink.AUDIT_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }

            if (EventEncoder.encode(MysterySpaceChainLink.REQUESTFULFILLED_EVENT).equals(topic)){
                //购买盲盒随机数回填
                contractEventSender.sendEvent(MysterySpaceChainLink.REQUESTFULFILLED_EVENT.getName(),
                        log.getTransactionHash() + SEPARATOR + log.getData());
                return;
            }

        });

    }

}

