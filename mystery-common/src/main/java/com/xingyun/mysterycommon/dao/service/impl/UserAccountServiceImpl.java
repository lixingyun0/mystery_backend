package com.xingyun.mysterycommon.dao.service.impl;

import com.xingyun.mysterycommon.dao.domain.entity.UserAccount;
import com.xingyun.mysterycommon.dao.mapper.UserAccountMapper;
import com.xingyun.mysterycommon.dao.service.IUserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 默认币 服务实现类
 * </p>
 *
 * @author xingyun
 * @since 2023-05-09
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

}
