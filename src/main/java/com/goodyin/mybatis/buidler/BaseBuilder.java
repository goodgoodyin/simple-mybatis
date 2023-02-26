package com.goodyin.mybatis.buidler;


import com.goodyin.mybatis.session.Configuration;

/**
 * 构建器的基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
