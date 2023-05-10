package com.goodyin.mybatis.reflection.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * getter调用者
 * @author GoodYin
 * @description
 * @date 2023/4/28 06:38
 */
public class GetFieldInvoke implements  Invoker {
    private Field field;

    public GetFieldInvoke(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
