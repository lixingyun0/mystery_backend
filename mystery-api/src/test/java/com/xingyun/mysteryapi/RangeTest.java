package com.xingyun.mysteryapi;

import com.alibaba.fastjson2.JSON;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RangeTest {

    public static void main(String[] args) {

        BigInteger fffff = new BigInteger("fffff", 16);
        String string = fffff.toString(10);
        //System.out.println(string);

        BigDecimal total = new BigDecimal("1048575");

        BigDecimal[] probabilities = new BigDecimal[]{new BigDecimal("0.001"),new BigDecimal("0.01"),new BigDecimal("0.3"),new BigDecimal("0.6890")};

        int[] range = new int[probabilities.length];
        for (int i = 0; i < probabilities.length; i++) {
            BigDecimal multiply = probabilities[i].multiply(total);
            System.out.println(multiply);
            if (i ==0){
                range[i] = multiply.intValue();
            } else if (i==probabilities.length-1) {
                range[i] = 1048575;
            }else {
                range[i] = range[i-1] + multiply.intValue();
            }
        }
        System.out.println(JSON.toJSONString(range));

        for (BigDecimal probability : probabilities) {
            System.out.println(probability.multiply(new BigDecimal("10000")));
        }
    }
}
