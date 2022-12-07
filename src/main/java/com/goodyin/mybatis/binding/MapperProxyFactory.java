package com.goodyin.mybatis.binding;

import com.goodyin.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * 代理工厂, 代理SqlSession类
 * @param <T>
 */
public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
