package com.goodyin.mybatis.test.dao;


public interface IUserDao {

    String queryUserName(String uId);

    String queryUserAge(String uId);

    String queryUserInfoById(String uId);


}
