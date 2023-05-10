package com.goodyin.mybatis.reflection.property;

import java.util.Locale;

/**
 * 属性命名器
 * @author GoodYin
 * @description
 * @date 2023/5/4 15:41
 */
public class PropertyNamer {

    public PropertyNamer() {
    }

    /**
     * 方法转换为属性
     * @param name
     * @return
     */
    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }


}
