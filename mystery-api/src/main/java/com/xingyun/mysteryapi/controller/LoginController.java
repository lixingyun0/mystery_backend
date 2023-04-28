package com.xingyun.mysteryapi.controller;

import com.alibaba.fastjson2.JSON;
import com.xingyun.mysteryapi.component.LoginRes;
import com.xingyun.mysteryapi.request.LoginWithWalletParam;
import com.xingyun.mysteryapi.request.TestSignParam;
import com.xingyun.mysteryapi.response.TestSignVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.utils.EthereumSignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.SignatureException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
@Api(tags = "User")
@Slf4j
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login/wallet")
    @ApiOperation("login with wallet")
    public R<LoginRes> loginWallet(@Valid @RequestBody LoginWithWalletParam param) throws SignatureException {
        //verify sign

        String originText = param.getOriginText();
        String trim = originText.split(":")[1].trim();
        if (Instant.now().toEpochMilli() - Long.parseLong(trim) > 1000*60*30){
            return R.failed("Signature expired");
        }

        boolean signatureValid = EthereumSignUtil.isSignatureValid(param.getAddress(), param.getSignature(), originText);
        if (!signatureValid){
            return R.failed("Invalid signature");
        }

        LoginRes loginRes = new LoginRes();
        String token = UUID.randomUUID().toString();

        loginRes.setToken(token);
        loginRes.setWalletAddress(param.getAddress());

        String oldToken = redisTemplate.opsForValue().get("USER_TOKEN_ADDRESS" + param.getAddress());
        if (StringUtils.isNotBlank(oldToken)) {
            redisTemplate.delete("USER_TOKEN" + oldToken);
        }
        redisTemplate.opsForValue().set("USER_TOKEN" + token, JSON.toJSONString(loginRes), 7, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("USER_TOKEN_ADDRESS" + param.getAddress(), token, 7, TimeUnit.DAYS);

        return R.ok(loginRes);

    }


    @PostMapping("/test/sign")
    @ApiOperation("签名测试接口")
    public R<TestSignVo> testSign(@Valid @RequestBody TestSignParam param) throws SignatureException {
        //verify sign

        String s1 = EthereumSignUtil.signMessage(param.getOriginText(), param.getPrivateKey());
        String s2 = EthereumSignUtil.signMessage2(param.getOriginText(), param.getPrivateKey());
        TestSignVo vo = new TestSignVo();
        vo.setSign1(s1);
        vo.setSign2(s2);
        return R.ok(vo);

    }


}
