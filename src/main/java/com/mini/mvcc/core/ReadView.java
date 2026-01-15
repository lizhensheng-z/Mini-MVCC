package com.mini.mvcc.core;

import lombok.Data;

import java.util.Set;

/**
 * 读快照：当前事务id，活跃事务id列表，最小事务id，最大事务id（当前全局最大事务id+1）
 */
@Data
public class ReadView {

    private Long minTrxId;

    private Set<Long> activeTrxIds;

    private Long creatorTrxId;

    private Long maxTrxId;
}
