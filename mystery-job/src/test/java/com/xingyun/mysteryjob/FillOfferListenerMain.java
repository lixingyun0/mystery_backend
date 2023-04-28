package com.xingyun.mysteryjob;

import com.alibaba.fastjson2.JSON;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FillOfferListenerMain {
    private static Logger logger = LoggerFactory.getLogger(FillOfferListenerMain.class);

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

    /**
     * 12:14:07.820 [main] INFO com.xingyun.lotteryjob.FillOfferListenerMain - contract listen data:Log{removed=false, logIndex='0x0', transactionIndex='0x0', transactionHash='0xf56218bec3beed767b1099f2875dfb57c607e7cee33da5211918c200a640a7cb', blockHash='0xbb9ba4cc7f396419a1c2cc489d673091dd184b5b5fe2c7bd5721274d3f633b58', blockNumber='0x32a078', address='0xc687c11c27d35433d353bf1c3abaf73420b3f13b', data='0x0000000000000000000000000000000000000000000000000000000000000024', type='null', topics=[0x2c15e9ad62f5ff0c54ae1e95b5e577122a9adf67048e9acc4d8c20ec854c50cc]}
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        //0x2c15e9ad62f5ff0c54ae1e95b5e577122a9adf67048e9acc4d8c20ec854c50cc
        String event = "DepositEvent(uint256,address[],uint256[])";
        byte[] input = event.getBytes();
        byte[] hash = Hash.sha3(input);
        System.out.println(Numeric.toHexString(hash));
        listen();
    }

    public static void listen() throws IOException {

        //获取启动时监听的区块
        Request<?, EthBlockNumber> request = web3.ethBlockNumber();
        BigInteger fromblock = request.send().getBlockNumber();
        System.out.println(fromblock);

        EthFilter ethFilter = new EthFilter(DefaultBlockParameter.valueOf(fromblock),
                //如果监听不到这里的地址可以把 0x 给去掉
                DefaultBlockParameterName.LATEST, "0x756CD7b5C8916D16335874a04Fa88CDb0313ec44");

        //ethFilter.addSingleTopic(EventEncoder.encode(MysterySpaceChainlink.PUBLISHMYSTERY_EVENT));
        logger.info("Starting listen offer filled event... ");

        Disposable disposable = web3.ethLogFlowable(ethFilter).subscribe(log -> {

            List<String> topics = log.getTopics();
            String topic = topics.get(0);

            if (EventEncoder.encode(MysterySpaceChainLink.PUBLISHMYSTERY_EVENT).equals(topic)){
                //发布盲盒
            }
            if (EventEncoder.encode(MysterySpaceChainLink.MINT_EVENT).equals(topic)){
                //购买盲盒
            }
            if (EventEncoder.encode(MysterySpaceChainLink.CLAIM_EVENT).equals(topic)){
                //领取盲盒奖励
            }
            if (EventEncoder.encode(MysterySpaceChainLink.DEPOSIT_EVENT).equals(topic)){
                //发行方补币
            }
            if (EventEncoder.encode(MysterySpaceChainLink.STOPSALE_EVENT).equals(topic)){
                //发行方停售
            }
            if (EventEncoder.encode(MysterySpaceChainLink.PUSHLISHERCLAIM_EVENT).equals(topic)){
                //发行方提取销售额
            }
            if (EventEncoder.encode(MysterySpaceChainLink.AUDIT_EVENT).equals(topic)){
                //管理员审核盲盒
            }
            if (EventEncoder.encode(MysterySpaceChainLink.REQUESTFULFILLED_EVENT).equals(topic)){
                //购买盲盒随机数回填
            }


            logger.info("contract listen data:{}", log.toString());

            String transactionHash = log.getTransactionHash();
            /*BigInteger amount = HexUtil.toBigInteger(data.substring(0, 64));
            BigInteger totalPrice = HexUtil.toBigInteger(data.substring(64, 128));
            String refer = (data.substring(128, 192)).toLowerCase();
            String buyerAddress = ("0x" + topics.get(1).substring(26)).toLowerCase();*/

            System.out.println("+++++++++++++++++++");
            System.out.println(log.getData());
            System.out.println(JSON.toJSONString(topics));
            //0xCc40CCB613383420ab19fF22722d74c51d59145F


        });

    }

}

