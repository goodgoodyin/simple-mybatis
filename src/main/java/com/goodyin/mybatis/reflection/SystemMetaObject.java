package com.goodyin.mybatis.reflection;

import com.goodyin.mybatis.reflection.factory.DefaultObjectFactory;
import com.goodyin.mybatis.reflection.factory.ObjectFactory;
import com.goodyin.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.goodyin.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * @author GoodYin
 * @description 一些系统级别的元对象
 * @date 2023/5/7 07:41
 */
public class SystemMetaObject {

    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    public SystemMetaObject() {
    }


    private static class NullObject {

    }

    /**
     * 把对象封装成 MetaObject 对象
     * @param object
     * @return
     */
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }


}
