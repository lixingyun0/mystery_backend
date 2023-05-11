package com.xingyun.mysterycommon.dao.service.impl;

import com.xingyun.mysterycommon.dao.domain.entity.Leaderboard;
import com.xingyun.mysterycommon.dao.mapper.LeaderboardMapper;
import com.xingyun.mysterycommon.dao.service.ILeaderboardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 排行榜 服务实现类
 * </p>
 *
 * @author xingyun
 * @since 2023-05-11
 */
@Service
public class LeaderboardServiceImpl extends ServiceImpl<LeaderboardMapper, Leaderboard> implements ILeaderboardService {

}
