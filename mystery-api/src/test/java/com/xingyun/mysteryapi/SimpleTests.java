package com.xingyun.mysteryapi;

import cn.hutool.crypto.digest.MD5;
import com.xingyun.mysterycommon.base.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class SimpleTests {

    private static void lottery() {
        String url = "https://oyen.api.storeapi.net/api/119/259";


        Map<String, String> params = new HashMap<>();
        params.put("appid", "27059");
        params.put("format", "json");

        params.put("time", String.valueOf(System.currentTimeMillis() / 1000));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        String unsign = "appid27059formatjsontime" + params.get("time") + "acaa0c2c8e1f860bee7e148f026295b2";
        //String unsign = "appid27059formatjson"+"acaa0c2c8e1f860bee7e148f026295b2";

        params.put("sign", MD5.create().digestHex(unsign));
        //System.out.println(MD5.create().digestHex(unsign));

        String s = HttpUtil.postForm(url, params, headers);
        System.out.println(s);
    }

    private static void info() {
        String url = "https://oyen.api.storeapi.net/api/119/261";


        Map<String, String> params = new HashMap<>();
        params.put("appid", "27059");
        params.put("format", "json");

        params.put("time", String.valueOf(System.currentTimeMillis() / 1000));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        String unsign = "appid27059formatjsontime" + params.get("time") + "acaa0c2c8e1f860bee7e148f026295b2";
        //String unsign = "appid27059formatjson"+"acaa0c2c8e1f860bee7e148f026295b2";

        params.put("sign", MD5.create().digestHex(unsign));
        //System.out.println(MD5.create().digestHex(unsign));

        String s = HttpUtil.postForm(url, params, headers);
        System.out.println(s);
    }

    public static void main(String[] args) {
        info();

    }
}
