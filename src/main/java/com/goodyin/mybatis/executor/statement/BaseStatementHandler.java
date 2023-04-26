package com.goodyin.mybatis.executor.statement;

import com.goodyin.mybatis.executor.Executor;
import com.goodyin.mybatis.executor.resultset.ResultSetHandle;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 语句处理抽象基类
 */
public abstract class BaseStatementHandler implements StatementHandle {

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandle resultSetHandle;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;
        this.parameterObject = parameterObject;
        this.resultSetHandle = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantStatement(connection);
            // 配置设置
            statement.setQueryTimeout(50);
            statement.setFetchSize(100000);
        } catch (Exception e) {
            throw new RuntimeException("创建处理器报错：" + e, e);
        }
        return statement;
    }

    protected abstract Statement instantStatement(Connection connection) throws SQLException;
}
