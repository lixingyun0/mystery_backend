package com.xingyun.mysterycommon.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xingyun.mysterycommon.base.HttpUtil;
import com.xingyun.mysterycommon.dto.TokenInfoDto;

import java.math.BigDecimal;

public class CoingeckoUtil {

    public static TokenInfoDto getTokenInfo(String chain,String contractAddress){
        String url = "https://api.coingecko.com/api/v3/coins/%s/contract/%s";
        String s = HttpUtil.get(url,chain,contractAddress);
        if (s.contains("error")){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(s);
        TokenInfoDto tokenInfoDto = new TokenInfoDto();
        tokenInfoDto.setName(jsonObject.getString("name"));
        tokenInfoDto.setDecimals(jsonObject.getJSONObject("detail_platforms").getJSONObject(chain).getInteger("decimal_place"));
        tokenInfoDto.setSymbol(jsonObject.getString("symbol"));
        tokenInfoDto.setIconUrl(jsonObject.getJSONObject("image").getString("small"));
        BigDecimal currentPrice = jsonObject.getJSONObject("market_data").getJSONObject("current_price").getBigDecimal("usd");
        tokenInfoDto.setCurrentPrice(currentPrice);
        return tokenInfoDto;
    }

    public static void main(String[] args) {
        TokenInfoDto tokenInfo = getTokenInfo("ethereum", "0xf203Ca1769ca8e9e8FE1DA9D147DB68B6c919817");
        System.out.println(tokenInfo);
    }
}
