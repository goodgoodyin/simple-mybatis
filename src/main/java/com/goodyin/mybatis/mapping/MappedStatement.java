package com.goodyin.mybatis.mapping;

import com.goodyin.mybatis.session.Configuration;

import java.util.Map;

/**
 * 映射语句类
 */
public class MappedStatement {

    // 配置信息
    private Configuration configuration;
    // sql id
    private String id;
    private SqlCommandType sqlCommandType;
    private BoundSql boundSql;

    public MappedStatement() {
    }


    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert this.mappedStatement.configuration != null;
            assert this.mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }
}
