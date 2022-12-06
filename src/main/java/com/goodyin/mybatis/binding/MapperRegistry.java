package com.goodyin.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import com.goodyin.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器注册机
 */
public class MapperRegistry {

    // 所有映射器代理
    private final Map<Class<?>, MapperProxyFactory<?>> knowMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knowMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("type : " + type + "没有找到对应的mapper工厂");
        }

        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("获取mapper实例报错", e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new RuntimeException("type:" + type + "已经存在");
            }
        }
        // 注册映射器代理工厂
        knowMappers.put(type, new MapperProxyFactory<>(type));
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knowMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }

    }


}
