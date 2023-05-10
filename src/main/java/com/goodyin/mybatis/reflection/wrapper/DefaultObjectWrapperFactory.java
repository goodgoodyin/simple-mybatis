package com.goodyin.mybatis.reflection.wrapper;

import com.goodyin.mybatis.reflection.MetaObject;

/**
 *  默认包装工厂
 * @author GoodYin
 * @description
 * @date 2023/5/7 07:15
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {


    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }


}
