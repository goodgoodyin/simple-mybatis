package com.goodyin.mybatis.session.defaults;

import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.session.SqlSession;
import com.goodyin.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
