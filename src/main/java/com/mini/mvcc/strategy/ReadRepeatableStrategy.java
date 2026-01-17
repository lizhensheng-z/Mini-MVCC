package com.mini.mvcc.strategy;

import com.mini.mvcc.core.ReadView;


public class ReadRepeatableStrategy implements IsolationStrategy{

    public static ThreadLocal<ViewContext> viewCache  = new ThreadLocal<>();


    @Override
    public ReadView getReadView(long trxId) {
        ViewContext viewContext = viewCache.get();
        if (viewContext!=null && viewContext.trxId == trxId){
            return viewContext.view;
        }
        ReadView readView = new ReadView(trxId);
        viewCache.set(new ViewContext(trxId,readView));
        return readView;
    }

    /**
     * 读视图上下文包装
     */
    private static final class ViewContext {
        final long trxId;
        final ReadView view;

        ViewContext(long trxId, ReadView view) {
            this.trxId = trxId;
            this.view = view;
        }
    }
}
