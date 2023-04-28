package com.xingyun.mysteryjob;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class PublishTest {
    public static void main(String[] args) {
        BigInteger bigInt = Numeric.toBigInt("0x0000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(bigInt);
    }
}
