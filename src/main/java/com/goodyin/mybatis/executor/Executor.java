package com.goodyin.mybatis.executor;

import com.goodyin.mybatis.executor.resultset.ResultSetHandle;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.ResultHandler;
import com.goodyin.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * SQL 执行器
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
}
