package com.goodyin.mybatis.session.defaults;

import cn.hutool.core.collection.CollUtil;
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

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T)("你被代理了!" + statement);
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
        try {
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            Environment environment = configuration.getEnvironment();
            BoundSql boundSql = mappedStatement.getBoundSql();

            // 创建连接
            Connection connection = environment.getDataSource().getConnection();
            // 创建对象
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[])param)[0].toString()));
            // 执行sql查询
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> res = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
            if (CollUtil.isEmpty(res)) {
                return null;
            }
            return res.get(0);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;

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
