package com.goodyin.mybatis.executor.statement;

import com.goodyin.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 语句处理器
 */
public interface StatementHandle {

    /**
     * 准备语句
     * @param connection
     * @return
     */
    Statement prepare(Connection connection);

    /**
     * 参数化
     * @param statement
     */
    void parameterize(Statement statement) throws SQLException;

    /**
     * 执行查询
     * @param statement
     * @param resultHandler
     * @param <E>
     * @return
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

}
