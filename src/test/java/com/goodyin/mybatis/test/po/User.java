package com.goodyin.mybatis.test.po;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String userId;
    private String userHead;
    private Date createTime;
    private Date updateTime;
}
