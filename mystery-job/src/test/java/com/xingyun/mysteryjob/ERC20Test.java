package com.xingyun.mysteryjob;

import com.xingyun.mysterycommon.contract.ERC20;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.concurrent.TimeUnit;

public class ERC20Test {

    private static String publicRpc = "https://sepolia.infura.io/v3/99919094740a4b57a459737deec46006";


    public static void main(String[] args) throws Exception {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);

        OkHttpClient okHttpClient = builder.build();

        Web3j web3j = Web3j.build(new HttpService(publicRpc,okHttpClient));

        ReadonlyTransactionManager readonlyTransactionManager = new ReadonlyTransactionManager(web3j, "0xB6C595dE75174146bb9c088E0d3E50274839266B");

        String contract = "0xA344313483521f7DF75494C8aFEc7e25a424A6aD";
        ERC20 erc20 = ERC20.load(contract, web3j, readonlyTransactionManager,new DefaultGasProvider());

        String send = erc20.symbol().send();
        System.out.println(send);
    }
}
