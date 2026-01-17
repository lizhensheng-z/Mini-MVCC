package com.mini.mvcc.core;

import jdk.internal.net.http.frame.GoAwayFrame;
import lombok.Data;

import java.util.Set;

/**
 * 读快照：当前事务id，活跃事务id列表，最小事务id，最大事务id（当前全局最大事务id+1）
 */
@Data
public class ReadView {

    private final Long minTrxId;

    private final Set<Long> activeTrxIds;

    private final Long creatorTrxId;

    private final Long maxTrxId;

    /**
     * 生成读视图
     * @param creatorTrxId
     */
    public ReadView(long creatorTrxId){
        ReadViewSnapShot readViewSnapShot = GlobalTransactionManage.createReadViewSnapShot(creatorTrxId);
        this.creatorTrxId = creatorTrxId;
        this.activeTrxIds = readViewSnapShot.activeTrxIds;
        this.minTrxId = readViewSnapShot.minTrxId;
        this.maxTrxId = readViewSnapShot.maxTrxId;
    }

    public boolean isVisible(long currentTrxId){
        //检查在不在黑名单中
        if(GlobalTransactionManage.INVISIBLE_DATA.contains(currentTrxId)){
            return false;
        }
        //当前事务id的版本比最小事务id小，说明当前版本可见
        if(currentTrxId<this.minTrxId){
            return true;
        }
        //当前事务id操作的是自己的版本，可见
        if(currentTrxId == this.creatorTrxId){
            return true;
        }
        //当前事务id 是之后的版本 不可见
        if(currentTrxId>=maxTrxId){
            return false;
        }

        //当前事务在活跃id列表中，未提交不可见
        if(activeTrxIds.contains(currentTrxId)){
            return false;
        }


        return true;
    }
}
