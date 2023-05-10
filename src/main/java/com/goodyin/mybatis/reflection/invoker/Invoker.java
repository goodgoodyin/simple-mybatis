package com.goodyin.mybatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * 调用者
 * @author GoodYin
 * @description
 * @date 2023/4/27 07:01
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException;

    Class<?> getType();
}
