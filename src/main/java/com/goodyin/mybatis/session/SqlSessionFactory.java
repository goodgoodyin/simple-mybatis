package com.goodyin.mybatis.session;

/**
 * 工厂模式接口，构建SqlSession的工厂
 */
public interface SqlSessionFactory {

    SqlSession openSession();

}
