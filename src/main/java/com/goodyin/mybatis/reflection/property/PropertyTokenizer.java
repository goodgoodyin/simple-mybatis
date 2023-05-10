package com.goodyin.mybatis.reflection.property;

import java.util.Iterator;

/**
 * 属性分解标记
 * @author GoodYin
 * @description
 * @date 2023/5/5 06:28
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {
    private String name;

    private String indexedName;

    private String index;

    private String children;

    /**
     * 解析一个属性表达式字符串，例如 "班级[0].学生.成绩"，将其拆分成属性名和子属性的字符串
     * @param fullname
     */
    public PropertyTokenizer(String fullname) {
        int delim = fullname.indexOf(".");
        if (delim > -1) {
            name = fullname.substring(0, delim);
            children = fullname.substring(delim + 1);
        } else {
            name = fullname;
            children = null;
        }
        indexedName = name;
        // 把中括号里的数字解析出来
        delim = name.indexOf('[');
        if (delim > -1) {
            index = name.substring(delim + 1, name.length() - 1);
            name = name.substring(0, delim);
        }
    }

    public String getName() {
        return name;
    }

    public String getIndexedName() {
        return indexedName;
    }

    public String getIndex() {
        return index;
    }

    public String getChildren() {
        return children;
    }

    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return children != null;
    }

    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
