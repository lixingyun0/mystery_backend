package com.xingyun.mysteryapi;

public class Test3 {

    public Integer num = 100;

    public static void main(String[] args) {

        int length = "00000000000000000000000000000000000000000000000000000000000003b5".length();
        System.out.println(length);

        System.out.println("0x723f41bD65DB7D8274b0723bc26Cbee23cBa5e2F".length());

        //3b5
        //3 * 16 **2 + 11 * 16 + 5 =
        System.out.println(3 * 16 * 16 + 11 * 16 + 5);
    }
}
