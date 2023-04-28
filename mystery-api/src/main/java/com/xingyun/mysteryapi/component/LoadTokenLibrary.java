package com.xingyun.mysteryapi.component;

import com.xingyun.mysterycommon.dao.domain.entity.TokenLibrary;
import com.xingyun.mysterycommon.dao.service.ITokenLibraryService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoadTokenLibrary {

    @Autowired
    private ITokenLibraryService tokenLibraryService;

    public Map<String, TokenLibrary> tokenLibraryMap;

    public String getTokenValue(String contractAddress,String amount){
        return new BigDecimal(amount).divide(new BigDecimal(Math.pow(10,tokenLibraryMap.get(contractAddress.toLowerCase()).getDecimals()))).toPlainString();
    }

    @PostConstruct
    public void load(){
        List<TokenLibrary> tokenLibraryList = tokenLibraryService.lambdaQuery().list();
        tokenLibraryMap = tokenLibraryList.stream().collect(Collectors.toMap(TokenLibrary::getAddress,tokenLibrary -> tokenLibrary));
    }


}
