package com.goodyin.mybatis.test.dao;


import com.goodyin.mybatis.test.po.User;

public interface IUserDao {

    String queryUserName(String uId);

    String queryUserAge(String uId);

    User queryUserInfoById(Long uId);


}
