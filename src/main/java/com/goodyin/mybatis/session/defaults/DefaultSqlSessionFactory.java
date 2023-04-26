package com.goodyin.mybatis.session.defaults;

import com.goodyin.mybatis.executor.Executor;
import com.goodyin.mybatis.mapping.Environment;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.SqlSession;
import com.goodyin.mybatis.session.SqlSessionFactory;
import com.goodyin.mybatis.session.TransactionIsolationLevel;
import com.goodyin.mybatis.transaction.Transaction;
import com.goodyin.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction transaction = null;
        try {
            Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            transaction = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);

            // 创建执行器
            Executor executor = configuration.newExecutor(transaction);
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert transaction != null;
                transaction.close();
            } catch (SQLException ignore) {

            }
            throw  new RuntimeException("打开会话报错");
        }

    }
}
