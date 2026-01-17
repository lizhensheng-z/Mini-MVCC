package com.mini.mvcc.strategy;

import com.mini.mvcc.core.ReadView;
import com.mini.mvcc.core.ReadViewSnapShot;

/**
 * 策略模式实现 RC和RR隔离级别下ReadView的生成时机
 */
public interface IsolationStrategy {

   ReadView getReadView(long trxId);
}
