package com.mini.mvcc.strategy;

import com.mini.mvcc.core.ReadView;

public class ReadCommitStrategy implements IsolationStrategy{
    /**
     *
     * @return RC隔离级别：每次当前读都生成一个ReadView
     */
    @Override
    public ReadView getReadView(long trxId) {
        return new ReadView(trxId);
    }
}
