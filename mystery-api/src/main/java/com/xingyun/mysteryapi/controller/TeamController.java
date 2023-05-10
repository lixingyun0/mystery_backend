package com.xingyun.mysteryapi.controller;

import com.xingyun.mysteryapi.common.LoginSession;
import com.xingyun.mysteryapi.config.Permission;
import com.xingyun.mysteryapi.response.WaitClaimAmountVo;
import com.xingyun.mysterycommon.base.R;
import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "团队")
@RequestMapping("team")
public class TeamController {

    @Autowired
    private IUserAccountService userAccountService;





}
