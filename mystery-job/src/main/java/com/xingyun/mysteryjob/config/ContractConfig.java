package com.xingyun.mysteryjob.config;

import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

@Configuration
public class ContractConfig {

    @Value("${contract.mystery_address}")
    private String mysteryContractAddress;

    @Value("${contract.owner}")
    private String contractOwner;

    @Autowired
    private Web3j web3j;

    @Bean
    public MysterySpaceChainLink mysterySpaceChainlink(){

        return MysterySpaceChainLink.load(mysteryContractAddress,web3j,
                Credentials.create("68c67d8515a6a78527a9609be25a504fd75b3227d00c98baed5917fb96b4fe28"),new DefaultGasProvider());
    }

    @Bean
    public ReadonlyTransactionManager readonlyTransactionManager(){
        return new ReadonlyTransactionManager(web3j,contractOwner);
    }
}
