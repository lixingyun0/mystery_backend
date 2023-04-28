package com.xingyun.mysteryjob;

import cn.hutool.core.util.HexUtil;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class DataTest {

    public static void main(String[] args) throws IOException {
        //0x2c15e9ad62f5ff0c54ae1e95b5e577122a9adf67048e9acc4d8c20ec854c50cc
        String event = "DepositEvent(uint256,address[],uint256[])";
        byte[] input = event.getBytes();
        byte[] hash = Hash.sha3(input);
        System.out.println(Numeric.toHexString(hash));


        String data = "0x0000000000000000000000000000000000000000000000000000000000000002a5a6bcbf7cf36d0a8e00b089843225ff152f0da41a3041112b9ff6bf97e4491f000000000000000000000000ede13d9460049adadc1f5717ae004c076389fa32";

        BigInteger boxId = Numeric.toBigInt(data.substring(0,66));
        BigInteger requestId = Numeric.toBigInt(data.substring(66,130));
        String address =  "0x"+ data.substring(154);

        System.out.println(boxId);
        System.out.println(requestId);
        System.out.println(address);

        System.out.println("74926219319814039723331644252661284317205414989284998504875652264089931237663".length());

        System.out.println("======");
        System.out.println(requestId.longValue());
        System.out.println(requestId.toString());
        System.out.println(requestId.toString(16));
        System.out.println("======");




        //listen();
    }
}
