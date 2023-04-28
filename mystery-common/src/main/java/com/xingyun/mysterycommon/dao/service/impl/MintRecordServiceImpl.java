package com.xingyun.mysterycommon.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingyun.mysterycommon.dao.domain.entity.MintRecord;
import com.xingyun.mysterycommon.dao.mapper.MintRecordMapper;
import com.xingyun.mysterycommon.dao.service.IMintRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购买盲盒记录表 服务实现类
 * </p>
 *
 * @author xingyun
 * @since 2023-04-23
 */
@Service
public class MintRecordServiceImpl extends ServiceImpl<MintRecordMapper, MintRecord> implements IMintRecordService {

}
