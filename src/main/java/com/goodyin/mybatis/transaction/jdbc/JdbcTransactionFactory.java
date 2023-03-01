package com.goodyin.mybatis.transaction.jdbc;

import com.goodyin.mybatis.session.TransactionIsolationLevel;
import com.goodyin.mybatis.transaction.Transaction;
import com.goodyin.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource date, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit) {
        return new JdbcTransaction(date, transactionIsolationLevel, autoCommit);
    }


}
