package com.goodyin.mybatis.session.defaults;

import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.session.SqlSession;
import com.goodyin.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
