package com.goodyin.mybatis.session;

import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.datasource.druid.DruidDataSourceFactory;
import com.goodyin.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.goodyin.mybatis.datasource.unpooled.UnPooledDataSource;
import com.goodyin.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import com.goodyin.mybatis.mapping.Environment;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.goodyin.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置项
 */
public class Configuration {

    // 环境
    private Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射语句Map
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<?> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Configuration setEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }
}
