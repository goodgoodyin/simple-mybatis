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
    private String parameterType;
    private String resultType;
    private String sql;
    private Map<Integer, String> parameter;

    public MappedStatement() {
    }


    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType, String resultType, String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.resultType = resultType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.parameter = parameter;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sql = sql;
        }

        public MappedStatement Builder() {
            assert this.mappedStatement.configuration != null;
            assert this.mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }
}
