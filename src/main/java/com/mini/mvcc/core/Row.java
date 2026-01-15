package com.mini.mvcc.core;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MySQL 的一行数据有哪些信息？
 */
@Data
public class Row {

    //主键
    private Long id;

    //数据行
    private Map<String,Object> dataColumn;

    //最后操作当前数据的事务 id
    private Long trxId;

    //隐藏的roll point指针
    private Row rollPoint;

    //行锁
    private ReentrantLock Lock;

    //逻辑删除
    private boolean isDeleted;

}
