package com.xingyun.mysterycommon.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingyun.mysterycommon.dao.domain.entity.TransactionRecord;
import com.xingyun.mysterycommon.dao.mapper.TransactionRecordMapper;
import com.xingyun.mysterycommon.dao.service.ITransactionRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易记录表 服务实现类
 * </p>
 *
 * @author xingyun
 * @since 2023-04-23
 */
@Service
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecord> implements ITransactionRecordService {

}
