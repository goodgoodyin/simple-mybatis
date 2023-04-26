package com.goodyin.mybatis.executor;

import com.goodyin.mybatis.executor.statement.StatementHandle;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.ResultHandler;
import com.goodyin.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 执行器具体实现
 */
public class simpleExecutor extends BaseExecutor{

    public simpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandle handle = configuration.newStatementHandle(this, ms, parameter, resultHandler, boundSql);

            Connection connection = transaction.getConnection();
            Statement statement = handle.prepare(connection);
            handle.parameterize(statement);
            return handle.query(statement, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
