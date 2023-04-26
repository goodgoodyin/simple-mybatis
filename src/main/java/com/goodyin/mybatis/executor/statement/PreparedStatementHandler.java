package com.goodyin.mybatis.executor.statement;

import com.goodyin.mybatis.executor.Executor;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 预处理语句处理器
 */
public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
    }


    @Override
    protected Statement instantStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        // 预处理sql
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement)statement;
        // 先简单处理一个参数
        ps.setLong(1, Long.parseLong(((Object[])parameterObject)[0].toString()));

    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandle.<E>handleResultSets(ps);
    }
}
