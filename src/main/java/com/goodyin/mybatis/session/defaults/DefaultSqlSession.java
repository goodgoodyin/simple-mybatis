package com.goodyin.mybatis.session.defaults;

import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.SqlSession;

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

    @Override
    public <T> T selectOne(String statement, Object param) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T)("你被代理了!" + "方法： " + statement + "入参：" + param
                + "\n 待执行sql：" + mappedStatement.getSql());
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
