package com.xingyun.mysterycommon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xingyun.mysterycommon.base.HttpUtil;
import com.xingyun.mysterycommon.contract.ERC20;
import com.xingyun.mysterycommon.dao.domain.entity.TokenLibrary;
import com.xingyun.mysterycommon.dao.service.ITokenLibraryService;
import com.xingyun.mysterycommon.dto.TokenInfoDto;
import com.xingyun.mysterycommon.utils.CoingeckoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
public class CoinServiceImpl {

    @Autowired
    private ITokenLibraryService tokenLibraryService;

    @Autowired
    private ReadonlyTransactionManager readonlyTransactionManager;

    @Autowired
    private Web3j web3j;

    public TokenInfoDto getTokenInfo(String chain, String contractAddress) throws Exception {
        TokenInfoDto tokenInfoDto = new TokenInfoDto();

        TokenLibrary tokenLibrary = tokenLibraryService.lambdaQuery()
                .eq(TokenLibrary::getAddress, contractAddress.toLowerCase()).one();
        if (tokenLibrary != null){
            tokenInfoDto.setName(tokenLibrary.getName());
            tokenInfoDto.setSymbol(tokenLibrary.getSymbol());
            tokenInfoDto.setDecimals(tokenLibrary.getDecimals());
            tokenInfoDto.setIconUrl(tokenLibrary.getIconUrl());
            tokenInfoDto.setCurrentPrice(tokenLibrary.getCurrentPrice());
            return tokenInfoDto;
        }

        //获取代币信息
        tokenInfoDto = CoingeckoUtil.getTokenInfo(chain, contractAddress);
        if (tokenInfoDto == null){
            tokenInfoDto = new TokenInfoDto();
            ERC20 erc20 = ERC20.load(contractAddress, web3j, readonlyTransactionManager, new DefaultGasProvider());
            String name = erc20.name().send();
            String symbol = erc20.symbol().send();
            BigInteger decimals = erc20.decimals().send();
            tokenInfoDto.setIconUrl("https://cryptologos.cc/logos/smooth-love-potion-slp-logo.png?v=024");
            tokenInfoDto.setDecimals(decimals.intValue());
            tokenInfoDto.setName(name);
            tokenInfoDto.setSymbol(symbol);
            tokenInfoDto.setCurrentPrice(BigDecimal.ZERO);
        }
        tokenLibrary = BeanUtil.toBean(tokenInfoDto,TokenLibrary.class);
        tokenLibrary.setAddress(contractAddress.toLowerCase());
        tokenLibraryService.save(tokenLibrary);

        return tokenInfoDto;
    }
}
