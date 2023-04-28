package com.xingyun.mysteryjob;

public class TestMain {
    public static void main(String[] args) {
       /* System.out.println(EventEncoder.encode(MysterySpaceChainLink.PUBLISHMYSTERY_EVENT));

        System.out.println(Numeric.toBigInt("0x0000000000000000000000000000000000000000000000000000000000000000"));

        String url = "https://api.coingecko.com/api/v3/coins/ethereum/contract/0x111111111117dc0aa78b770fa6a738034120c302";
        System.out.println(HttpUtil.get(url));*/

        String origin = "233123@0x42323";

        String[] split = origin.split("@");
        System.out.println(split.length);

        System.out.println("0x56629624bcf54e9803716f432b9213de6b6ca246abcca43481a2b4fbca6b3887".length());
    }
}
