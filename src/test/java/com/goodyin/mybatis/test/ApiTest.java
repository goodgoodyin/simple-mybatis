package com.goodyin.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.goodyin.mybatis.binding.MapperProxyFactory;
import com.goodyin.mybatis.binding.MapperRegistry;
import com.goodyin.mybatis.datasource.pooled.PooledConnection;
import com.goodyin.mybatis.datasource.pooled.PooledDataSource;
import com.goodyin.mybatis.io.Resources;
import com.goodyin.mybatis.session.SqlSession;
import com.goodyin.mybatis.session.SqlSessionFactory;
import com.goodyin.mybatis.session.SqlSessionFactoryBuilder;
import com.goodyin.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.goodyin.mybatis.test.dao.IUserDao;
import com.goodyin.mybatis.test.po.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
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

    /**
     * 代理工厂
     */
//    @Test
//    public void test_SqlSessionFactory() {
//        // 1、注册 Mapper，扫描包路径注册，在注册的时候就包装代理了
//        MapperRegistry registry = new MapperRegistry();
//        registry.addMappers("com.goodyin.mybatis.test.dao");
//
//        // 2、 从SqlSession工厂获取Session
//        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//
//        // 3、 获取映射器对象
//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//
//        // 4、执行结果
//        String res = userDao.queryUserAge("10001");
//        logger.info("测试结果:{}", res);
//    }

    /**
     *  1。工厂模式代理解析注册XML
     *  2。连接数据源查询,参数解析
     * @throws IOException
     */
    @Test
    public void test_SqlSessionFactory() throws IOException {

        // 1 从xml获取SqlSession
        // 读取xml资源
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        // 构建xml，注册xml映射器，解析数据库连接
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 查询
        User res = userDao.queryUserInfoById(1L);
        logger.info("测试结果: {}", res);
    }

    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis_db?useUnicode=true");
        pooledDataSource.setUsername("test");
        pooledDataSource.setPassword("test_test");
        // 持续获得链接
        while (true){
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            connection.close();
        }
    }


    /**
     * 连接池
     * @throws IOException
     */
    @Test
    public void test_SqlSessionFactoryPool() throws IOException {

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserInfoById(Long.valueOf(i));
            logger.info("测试结果：{}", JSON.toJSONString(user));
        }
    }


}
