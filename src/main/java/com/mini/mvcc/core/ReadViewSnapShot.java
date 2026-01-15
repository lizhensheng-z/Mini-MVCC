package com.mini.mvcc.core;


import java.util.Set;

/**
 * 某一时刻的读快照-唯一
 */
public final class ReadViewSnapShot {
    public final Long creatorTrxId;
    public final Long minTrxId;
    public final Set<Long> activeTrxIds;
    public final Long maxTrxId;
    public ReadViewSnapShot(Long creatorTrxId,Long minTrxId,Set<Long> activeTrxIds,Long maxTrxId){
        this.creatorTrxId = creatorTrxId;
        this.minTrxId = minTrxId;
        this.activeTrxIds = activeTrxIds;
        this.maxTrxId = maxTrxId;
    }


}
