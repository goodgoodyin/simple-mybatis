package com.goodyin.mybatis.session.defaults;

import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.session.SqlSession;

/**
 * 默认的SqlSession实现
 */
public class DefaultSqlSession implements SqlSession {

    private MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T)("你被代理了!" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object param) {
        return (T)("你被代理了!" + "方法： " + statement + "入参：" + param);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
