package com.xingyun.mysteryjob.controller;

import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.contract.MysterySpaceChainLink;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试")
@RequestMapping("test")
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("send1")
    public R send1(){
        redisTemplate.convertAndSend(MysterySpaceChainLink.PUBLISHMYSTERY_EVENT.getName(),"233123@0x42323");
        System.out.println("send1");
        return R.ok();
    }
}
