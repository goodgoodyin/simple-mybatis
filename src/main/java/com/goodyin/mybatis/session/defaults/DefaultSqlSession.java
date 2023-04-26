package com.goodyin.mybatis.session.defaults;

import cn.hutool.core.collection.CollUtil;
import com.goodyin.mybatis.executor.Executor;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.Environment;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认的SqlSession实现
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        configuration.getMappedStatement(statement);
        return this.selectOne(statement, null);
    }

    /**
     * 查询处理
     * @param statement
     * @param param
     * @param <T>
     * @return
     */
    @Override
    public <T> T selectOne(String statement, Object param)  {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        List<T> query = executor.query(mappedStatement, param, Executor.NO_RESULT_HANDLER, mappedStatement.getBoundSql());
        if (CollUtil.isEmpty(query)) {
            return null;
        }
        return query.get(0);

    }

    /**
     * 参数解析
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 遍历
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method = null;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
