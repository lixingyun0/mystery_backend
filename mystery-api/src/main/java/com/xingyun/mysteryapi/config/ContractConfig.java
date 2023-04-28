package com.xingyun.mysteryapi.config;

import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import static org.web3j.protocol.core.JsonRpc2_0Web3j.DEFAULT_BLOCK_TIME;

@Configuration
public class ContractConfig {

    @Value("${contract.mystery_address}")
    private String mysteryContractAddress;

    @Value("${contract.owner}")
    private String contractOwner;

    @Autowired
    private Web3j web3j;

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;

    @Bean
    public MysterySpaceChainLink mysterySpaceChainlink(){

        return MysterySpaceChainLink.load(mysteryContractAddress,web3j,
                Credentials.create("68c67d8515a6a78527a9609be25a504fd75b3227d00c98baed5917fb96b4fe28"),new DefaultGasProvider());
    }

    @Bean
    public ReadonlyTransactionManager readonlyTransactionManager(){
        return new ReadonlyTransactionManager(web3j,contractOwner);
    }

    @Bean
    public PollingTransactionReceiptProcessor pollingTransactionReceiptProcessor(){
        return new PollingTransactionReceiptProcessor(
                web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
    }
}
