package com.mini.mvcc.core;

import com.mini.mvcc.exception.TransactionException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  全局事务管理器：负责事务的开启，提交，回滚，分配事务id
 */
public class GlobalTransactionManage {
    /**
     * 原子类保证每次生成的 事务id递增
     */
    private static final AtomicLong GLOBAL_TRX_ID = new AtomicLong(0L);

    /**
     *
     * @return 生成 当前事务id ：获取最大事务id+1
     */
    public static Long generateTrxId(){
        return GLOBAL_TRX_ID.getAndIncrement();
    }

    /**
     * 事务线程存储 自身事务id
     */
    public static final ThreadLocal<Long> CURRENT_TRX_ID = new ThreadLocal<>();

    /**
     * 活跃事务id 列表 维护一个线程安全的set
     */
    public static final Set<Long> ACTIVE_TRX_IDS = Collections.synchronizedSet(new HashSet<>());

    /**
     * 开启事务
     * @return
     */
    public static Long begin(){

        //先生成 当前事务id
        Long currentTrxId = generateTrxId();
        System.out.println("当前线程"+Thread.currentThread().getName()+"开启事务，当前事务id："+currentTrxId);
        //存入 当前线程的threadLocal内
        CURRENT_TRX_ID.set(currentTrxId);
        //加入 活跃事务id列表
        ACTIVE_TRX_IDS.add(currentTrxId);
        return currentTrxId;
    }

    /**
     * 提交事务：把当前事务id移除活跃事务id列表，并且从threadLocal清除，避免内存泄漏
     *
     */
    public static void commit(){
        Long currentTrxId = CURRENT_TRX_ID.get();
        if(currentTrxId == null){
            throw new TransactionException("当前线程"+Thread.currentThread().getName()+"未开启事务，无法提交！");
        }
        ACTIVE_TRX_IDS.remove(currentTrxId);
        CURRENT_TRX_ID.remove();
    }
    public static final Set<Long> INVISIBLE_DATA = new HashSet<>();
    /**
     * 回滚事务：当前数据不可见
     * 简化处理：放入黑名单
     */
    public static void rollback(){
        Long currentTrxId =CURRENT_TRX_ID.get();
        if(currentTrxId == null){
            throw new TransactionException("当前线程"+Thread.currentThread().getName()+"未开启事务，无法回滚！");
        }
        //从活跃事务id列表 移除
        ACTIVE_TRX_IDS.remove(currentTrxId);
        CURRENT_TRX_ID.remove();
        //简化处理：加入不可见的黑名单
        INVISIBLE_DATA.add(currentTrxId);
    }

    /**
     *
     * @return 生成读快照
     */
    public static ReadViewSnapShot createReadViewSnapShot(Long creatorTrxId){
        Set<Long> mIdsCopy;
        long maxTrxId;
        //加锁，防止这一时刻的快照不一致
        synchronized (ACTIVE_TRX_IDS){
            //拷贝此刻的 活跃事务id列表
            mIdsCopy = new HashSet<>(ACTIVE_TRX_IDS);
            //获取当前时刻的最大事务id：全局最大+1
            maxTrxId = GLOBAL_TRX_ID.get()+1;
        }
        Long minTrxId = mIdsCopy.isEmpty() ? maxTrxId : mIdsCopy.stream().min(Long::compareTo).get();
        return new ReadViewSnapShot(creatorTrxId,minTrxId,mIdsCopy,maxTrxId);
    }
}
