package com.xingyun.mysteryjob.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Configuration
public class Web3jConfig {

    @Value("${web3j.public-rpc}")
    private String publicRpc;

    @Bean
    public Web3j web3j(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);

        OkHttpClient okHttpClient = builder.build();

        return Web3j.build(new HttpService(publicRpc,okHttpClient));
    }



}
