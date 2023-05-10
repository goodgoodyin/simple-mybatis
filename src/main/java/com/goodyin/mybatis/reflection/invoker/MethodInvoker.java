package com.goodyin.mybatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法调用者
 * @author GoodYin
 * @description
 * @date 2023/4/28 06:15
 */
public class MethodInvoker implements Invoker{
    private Class<?> type;
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        // 只有一个参数说明是get，没参数则是set
        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
