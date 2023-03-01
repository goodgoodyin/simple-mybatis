package com.goodyin.mybatis.buidler;


import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.type.TypeAliasRegistry;

/**
 * 构建器的基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }


    public Configuration getConfiguration() {
        return configuration;
    }
}
