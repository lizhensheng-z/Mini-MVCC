package com.mini.mvcc.core.exception;

/**
 * 事务相关异常
 */
public class TransactionException extends RuntimeException{
    public TransactionException(String message){
        super(message);
    }
    public TransactionException(String message,Throwable cause){
        super(message,cause);
    }
}
