package com.xingyun.mysteryjob;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransactionMain {

    private static Logger logger = LoggerFactory.getLogger(TransactionMain.class);

    private static String publicRpc = "https://sepolia.infura.io/v3/99919094740a4b57a459737deec46006";
    private static Web3j web3;
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);

        OkHttpClient okHttpClient = builder.build();

        web3 = Web3j.build(new HttpService(publicRpc,okHttpClient));

    }


    public static void main(String[] args) throws IOException {
        /*EthFilter ethFilter = new EthFilter(DefaultBlockParameter.valueOf(createContactBlock),
                //如果监听不到这里的地址可以把 0x 给去掉
                DefaultBlockParameterName.LATEST, "0x995F92069C97Bcf224d568DA4516FA175270Cc29");*/
        EthGetTransactionReceipt send = web3.ethGetTransactionReceipt("0x1f32f383c2e9bcf0a881e2da3792856bc23c459150856a3ee881a99f5800d368").send();
        TransactionReceipt result = send.getResult();
        System.out.println(result);
        List<Log> logs = result.getLogs();
        for (Log log : logs) {
            System.out.println("========");
            System.out.println(log);
        }

    }
}
