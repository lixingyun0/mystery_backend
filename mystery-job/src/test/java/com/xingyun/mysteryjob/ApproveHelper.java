package com.xingyun.mysteryjob;

import com.alibaba.fastjson2.JSON;
import com.xingyun.mysterycommon.contract.ERC20;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import okhttp3.OkHttpClient;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ApproveHelper {

    private static String publicRpc = "https://sepolia.infura.io/v3/99919094740a4b57a459737deec46006";
    //private static String publicRpc = "http://127.0.0.1:8545";

    private static Web3j web3;
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);

        OkHttpClient okHttpClient = builder.build();

        web3 = Web3j.build(new HttpService(publicRpc,okHttpClient));

    }

    public static void main(String[] args) throws Exception {

        Credentials credentials = Credentials.create("68c67d8515a6a78527a9609be25a504fd75b3227d00c98baed5917fb96b4fe28");

        System.out.println(credentials.getAddress());

        DefaultGasProvider defaultGasProvider = new DefaultGasProvider();

        String YUSD = "0x1615c24A1CBE893951cf1101B97334c0e38d27A8";
        String LUSD = "0x723f41bD65DB7D8274b0723bc26Cbee23cBa5e2F";
        String XUSD = "0xCA427712dB471a2242fB579990C8F74c1bd8Bb3C";


        ERC20 y = ERC20.load(YUSD, web3, credentials, defaultGasProvider);
        ERC20 l = ERC20.load(LUSD, web3, credentials, defaultGasProvider);
        ERC20 x = ERC20.load(XUSD, web3, credentials, defaultGasProvider);
        System.out.println("====start========");
        TransactionReceipt send1 = y.approve("0xf7E75082e8d4c9e01f07789f7e8A39731c6820d6", BigInteger.valueOf(100000000000000L)).send();
        System.out.println(send1.getTransactionHash());
        TransactionReceipt send2 = l.approve("0xf7E75082e8d4c9e01f07789f7e8A39731c6820d6", BigInteger.valueOf(100000000000000L)).send();
        System.out.println(send2.getTransactionHash());
        TransactionReceipt send = x.approve("0xf7E75082e8d4c9e01f07789f7e8A39731c6820d6", BigInteger.valueOf(100000000000000L)).send();
        System.out.println(send.getTransactionHash());

/*        RawTransactionManager transactionManager = new RawTransactionManager(web3, credentials);

        org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                "mint",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(BigInteger.ONE)),
                Collections.<TypeReference<?>>emptyList());
        String data = FunctionEncoder.encode(function);
        EthSendTransaction transaction = transactionManager.sendTransaction(defaultGasProvider.getGasPrice(),
                defaultGasProvider.getGasLimit(), "0xa344313483521f7df75494c8afec7e25a424a6ad", data, BigInteger.ZERO);
        String transactionHash = transaction.getTransactionHash();*/


    }
}
