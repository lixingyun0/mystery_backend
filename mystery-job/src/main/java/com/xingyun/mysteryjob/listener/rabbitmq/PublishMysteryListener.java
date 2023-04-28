package com.xingyun.mysteryjob.listener.rabbitmq;

import cn.hutool.core.bean.BeanUtil;
import com.rabbitmq.client.Channel;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBox;
import com.xingyun.mysterycommon.dao.domain.entity.MysteryBoxReward;
import com.xingyun.mysterycommon.dao.domain.entity.TokenLibrary;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxRewardService;
import com.xingyun.mysterycommon.dao.service.IMysteryBoxService;
import com.xingyun.mysterycommon.dao.service.ITokenLibraryService;
import com.xingyun.mysterycommon.dto.TokenInfoDto;
import com.xingyun.mysterycommon.service.impl.CoinServiceImpl;
import com.xingyun.mysterycommon.utils.CoingeckoUtil;
import com.xingyun.mysteryjob.component.CheckTransactionHelper;
import com.xingyun.mysterycommon.contract.ERC20;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class PublishMysteryListener {

    private Logger logger = LoggerFactory.getLogger(PublishMysteryListener.class);

    @Autowired
    private IMysteryBoxService mysteryBoxService;

    @Autowired
    private IMysteryBoxRewardService mysteryBoxRewardService;

    @Autowired
    private ITokenLibraryService tokenLibraryService;

    @Autowired
    private MysterySpaceChainLink contract;

    @Autowired
    private ReadonlyTransactionManager readonlyTransactionManager;

    @Autowired
    private Web3j web3j;

    @Autowired
    private CoinServiceImpl coinService;

    private static String CHAIN = "ethereum";

    @Autowired
    private CheckTransactionHelper checkTransactionHelper;

    public PublishMysteryListener(){
        System.out.println("Init PublishMysteryListener");
    }

    @RabbitListener(queues = {"PublishMystery"})
    @Transactional
    public void onMessage(Message message, Channel channel) throws IOException {
        String origin = new String(message.getBody(), StandardCharsets.UTF_8);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        logger.info("PublishMysteryListener:{}",origin);
        String[] split = origin.split("@");
        String txHash = split[0];
        boolean pass = checkTransactionHelper.checkTxHash(txHash, "PublishMystery");
        if (!pass){
            return;
        }
        boolean exists = mysteryBoxService.lambdaQuery().eq(MysteryBox::getTransactionHash, txHash).exists();
        if (exists){
            return;
        }
        String data = split[1];
        BigInteger boxId = Numeric.toBigInt(data);
        try {
            Tuple8<String, BigInteger, BigInteger, BigInteger, BigInteger, String, Boolean, Boolean> boxInfo = contract.getBoxInfo(boxId).send();
            Tuple4<List<BigInteger>, List<BigInteger>, List<String>, List<BigInteger>> boxRewardInfo = contract.getBoxRewardInfo(boxId).send();
            MysteryBox mysteryBox = new MysteryBox();
            mysteryBox.setBoxId(boxId.toString());
            mysteryBox.setCoin(boxInfo.component1());
            mysteryBox.setPrice(boxInfo.component2().toString());
            mysteryBox.setTotalSupply(boxInfo.component3().intValue());
            mysteryBox.setStocks(boxInfo.component4().intValue());
            mysteryBox.setIncome(boxInfo.component5().toString());
            mysteryBox.setOwnerAddress(boxInfo.component6().toLowerCase());
            mysteryBox.setCheckedFlag(boxInfo.component7());
            mysteryBox.setStopFlag(boxInfo.component8());
            mysteryBox.setFrozenFlag(false);
            mysteryBox.setTransactionHash(txHash);

            List<MysteryBoxReward> mysteryBoxRewardList = new ArrayList<>();
            BigDecimal worth =  BigDecimal.ZERO;
            for (int i = 0; i < boxRewardInfo.component1().size(); i++) {
                MysteryBoxReward mysteryBoxReward = new MysteryBoxReward();
                mysteryBoxReward.setBoxId(boxId.toString());
                mysteryBoxReward.setProbability(new BigDecimal(boxRewardInfo.component1().get(i)).divide(new BigDecimal("10000"),4, RoundingMode.HALF_DOWN));
                if (i ==0){
                    mysteryBoxReward.setMinNumber(0);
                    mysteryBoxReward.setMaxNumber(boxRewardInfo.component2().get(i).intValue());
                }else {
                    mysteryBoxReward.setMinNumber(boxRewardInfo.component2().get(i-1).intValue()+1);
                    mysteryBoxReward.setMaxNumber(boxRewardInfo.component2().get(i).intValue());
                }

                mysteryBoxReward.setTokenAddress(boxRewardInfo.component3().get(i));
                mysteryBoxReward.setRewardAmount(boxRewardInfo.component4().get(i).toString());


                BigDecimal theoretical = mysteryBoxReward.getProbability().multiply(new BigDecimal(mysteryBox.getTotalSupply()));
                if (theoretical.compareTo(BigDecimal.ONE) <0){
                    mysteryBoxReward.setStakingAmount(new BigDecimal(mysteryBoxReward.getRewardAmount()).setScale(0,RoundingMode.HALF_DOWN).toPlainString());
                }else {
                    mysteryBoxReward.setStakingAmount(mysteryBoxReward.getProbability()
                            .multiply(new BigDecimal(mysteryBox.getTotalSupply())
                                    .multiply(new BigDecimal(mysteryBoxReward.getRewardAmount()))).setScale(0,RoundingMode.HALF_DOWN).toPlainString());
                }


                TokenInfoDto tokenInfo = coinService.getTokenInfo(CHAIN, mysteryBoxReward.getTokenAddress());

                mysteryBoxReward.setTokenName(tokenInfo.getName());
                mysteryBoxReward.setSymbol(tokenInfo.getSymbol());
                mysteryBoxReward.setDecimals(tokenInfo.getDecimals());
                mysteryBoxReward.setTokenUrl(tokenInfo.getIconUrl());

                worth = worth.add(tokenInfo.getCurrentPrice().multiply(new BigDecimal(mysteryBoxReward.getStakingAmount()))
                        .divide(new BigDecimal(Math.pow(10,mysteryBoxReward.getDecimals())),6,RoundingMode.HALF_DOWN));

                mysteryBoxRewardList.add(mysteryBoxReward);
            }
            mysteryBox.setWorth(worth);
            TokenInfoDto tokenInfo = coinService.getTokenInfo(CHAIN, mysteryBox.getCoin());
            mysteryBox.setCoinName(tokenInfo.getName());
            mysteryBox.setCoinUrl(tokenInfo.getIconUrl());
            mysteryBox.setCoinSymbol(tokenInfo.getSymbol());
            mysteryBox.setCoinDecimals(tokenInfo.getDecimals());
            if (tokenInfo.getCurrentPrice().intValue() == 0){
                mysteryBox.setWorthPercent(new BigDecimal("1.0"));
            }else {
                mysteryBox.setWorthPercent(worth.divide(tokenInfo.getCurrentPrice().multiply(new BigDecimal(mysteryBox.getPrice()))
                        .multiply(new BigDecimal(mysteryBox.getTotalSupply())),2,RoundingMode.HALF_DOWN));
            }

            mysteryBoxService.save(mysteryBox);
            mysteryBoxRewardService.saveBatch(mysteryBoxRewardList);
        } catch (Exception e) {
            logger.error("查询盲盒详情失败:{}",boxId,e);
            throw new RuntimeException(e);
        }
    }

}
