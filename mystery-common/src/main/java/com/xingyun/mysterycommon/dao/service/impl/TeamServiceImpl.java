package com.xingyun.mysterycommon.dao.service.impl;

import com.xingyun.mysterycommon.dao.domain.entity.Team;
import com.xingyun.mysterycommon.dao.mapper.TeamMapper;
import com.xingyun.mysterycommon.dao.service.ITeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 团队 服务实现类
 * </p>
 *
 * @author xingyun
 * @since 2023-05-09
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements ITeamService {

}
