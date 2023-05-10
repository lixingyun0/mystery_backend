package com.xingyun.mysteryjob;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class NumericTests {

    public static void main(String[] args) {

        BigInteger bigInt = Numeric.toBigInt("0xaa36a7");
        System.out.println(bigInt);
    }
}
