package com.goodyin.mybatis.datasource.unpooled;

import com.goodyin.mybatis.datasource.DataSourceFactory;
import com.goodyin.mybatis.reflection.MetaObject;
import com.goodyin.mybatis.reflection.SystemMetaObject;

import javax.sql.DataSource;
import java.time.temporal.ValueRange;
import java.util.Properties;

/**
 * 无池化数据源工厂
 */
public class UnPooledDataSourceFactory implements DataSourceFactory {

    protected DataSource dataSource;

    public UnPooledDataSourceFactory() {
        this.dataSource = new UnPooledDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        MetaObject metaObject = SystemMetaObject.forObject(dataSource);
        for (Object key : properties.keySet()) {
            String propertyName = (String) key;
            if (metaObject.hasSetter(propertyName)) {
                String value = (String) properties.get(propertyName);
                Object convertValue = convertValue(metaObject, propertyName, value);
                metaObject.setValue(propertyName, convertValue);
            }
        }
    }

    @Override
    public DataSource getDataSource() {
      return dataSource;
    }

    /**
     * 根据setter的类型,将配置文件中的值强转成相应的类型
     */
    private Object convertValue(MetaObject metaObject, String propertyName, String value) {
        Object convertedValue = value;
        Class<?> targetType = metaObject.getSetterType(propertyName);
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            convertedValue = Long.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }
}
