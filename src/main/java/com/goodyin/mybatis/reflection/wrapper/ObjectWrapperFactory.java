package com.goodyin.mybatis.reflection.wrapper;

import com.goodyin.mybatis.executor.Executor;
import com.goodyin.mybatis.reflection.MetaObject;

/**
 * 对象包装工厂
 */
public interface ObjectWrapperFactory {

    /**
     * 判断有没有包装器
     * @param object
     * @return
     */
    boolean hasWrapperFor(Object object);

    /**
     * 得到包装器
     * @param metaObject
     * @return
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);
}
