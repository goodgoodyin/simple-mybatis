package com.goodyin.mybatis.session;

/**
 * sql管理，用来执行sql，获取映射器，管理事物
 */
public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object param);

    /**
     * 获取映射器
     * @param type
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> type);

    /**
     * 获取配置
     * @return
     */
    Configuration getConfiguration();
}
