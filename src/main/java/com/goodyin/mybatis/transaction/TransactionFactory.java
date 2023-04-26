package com.goodyin.mybatis.transaction;

import com.goodyin.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务工厂
 */
public interface TransactionFactory {

    /**
     * 根据 Connection 创建 Transaction
     * @param connection
     * @return
     */
    Transaction newTransaction(Connection connection);


    /**
     * 根据数据源和事务隔离级别创建 Transaction
     * @param date
     * @return
     */
    Transaction newTransaction(DataSource date, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit);

}
