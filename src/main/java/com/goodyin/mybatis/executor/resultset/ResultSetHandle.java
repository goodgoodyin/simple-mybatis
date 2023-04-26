package com.goodyin.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 结果处理器
 */
public interface ResultSetHandle {

    <E> List<E> handleResultSets(Statement statement) throws SQLException;
}
