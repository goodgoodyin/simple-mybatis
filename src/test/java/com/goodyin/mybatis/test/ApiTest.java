package com.goodyin.mybatis.test;

import com.goodyin.mybatis.binding.MapperProxyFactory;
import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.session.SqlSession;
import com.goodyin.mybatis.session.SqlSessionFactory;
import com.goodyin.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.goodyin.mybatis.test.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

//    @Test
//    public void test_MapperProxyFactory() {
//        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
//        Map<String, String> sqlSession = new HashMap<>();
//        sqlSession.put("com.goodyin.mybatis.test.dao.IUserDao.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
//        sqlSession.put("com.goodyin.mybatis.test.dao.IUserDao.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");
//        IUserDao userDao = factory.newInstance(sqlSession);
//        userDao.toString();
//        String res = userDao.queryUserAge("10001");
//        logger.info("测试结果：{}", res);
//    }

    @Test
    public void test_SqlSessionFactory() {
        // 1、注册 Mapper，扫描包路径注册，在注册的时候就包装代理了
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("com.goodyin.mybatis.test.dao");

        // 2、 从SqlSession工厂获取Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3、 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4、执行结果
        String res = userDao.queryUserAge("10001");
        logger.info("测试结果:{}", res);
 }
}
