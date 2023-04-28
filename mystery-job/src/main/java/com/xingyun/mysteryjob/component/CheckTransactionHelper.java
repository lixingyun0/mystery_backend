package com.xingyun.mysteryjob.component;

import com.xingyun.mysterycommon.dao.domain.entity.TransactionRecord;
import com.xingyun.mysterycommon.dao.service.ITransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckTransactionHelper {

    @Autowired
    private ITransactionRecordService transactionRecordService;

    public boolean checkTxHash(String txHash,String type){
        boolean exists = transactionRecordService.lambdaQuery().eq(TransactionRecord::getTransactionHash, txHash).exists();
        if (exists){
            return false;
        }
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTransactionHash(txHash);
        transactionRecord.setType(type);
        transactionRecordService.save(transactionRecord);
        return true;
    }
}
