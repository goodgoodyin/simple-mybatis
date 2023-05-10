package com.goodyin.mybatis.reflection.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * setter调用者
 * @author GoodYin
 * @description
 * @date 2023/4/28 06:40
 */
public class SetFieldInvoke implements Invoker {
    private Field field;

    public SetFieldInvoke(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
