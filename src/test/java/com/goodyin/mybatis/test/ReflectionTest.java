package com.goodyin.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.goodyin.mybatis.reflection.MetaObject;
import com.goodyin.mybatis.reflection.SystemMetaObject;
import com.goodyin.mybatis.reflection.factory.DefaultObjectFactory;
import com.goodyin.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GoodYin
 * @description 反射测试类
 * @date 2023/5/7 10:51
 */
public class ReflectionTest {

    private Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void test_reflection() {
        Teacher teacher = new Teacher();
        List<Teacher.Student> list = new ArrayList<>();
        list.add(new Teacher.Student());
        list.add(new Teacher.Student());
        teacher.setName("测试");
        teacher.setStudents(list);

        MetaObject metaObject = SystemMetaObject.forObject(teacher);
        logger.info("getGetterNames: {}", JSON.toJSONString(metaObject.getGetterNames()));
        logger.info("getSetterNames: {}", JSON.toJSONString(metaObject.getSetterNames()));
        logger.info("name的get方法参数值:{}", JSON.toJSONString(metaObject.getGetterType("name")));
        logger.info("students的set方法参数值:{}", JSON.toJSONString(metaObject.getGetterType("students")));
        logger.info("name的hasGetter:{}", metaObject.hasGetter("name"));
        logger.info("student.id(属性为对象)的hasGetter:{}", metaObject.hasGetter("student.id"));
        logger.info("获取name的属性值:{}", metaObject.getValue("name"));
        // 重新设计属性值
        metaObject.setValue("name", "张三");
        logger.info("设置name属性值:{}", metaObject.getValue("name"));
        // 设置属性值（集合）的元素值
        metaObject.setValue("students[0].id", "001");
        logger.info("获取student集合第一个元素的属性值:{}", JSON.toJSONString(metaObject.getValue("students[0].id")));
        // 设置属性（Map）的属性值
        Map<String, String> map = new HashMap<>();
        map.put("key", "123");
        metaObject.setValue("map", map);
        logger.info("获取集合第一个元素.map的值{}", JSON.toJSONString(metaObject.getValue("map")));
        // 对象序列化
        logger.info("对象值:{}", teacher.toString());
    }




    static class Teacher {

        private String name;

        private double price;

        private List<Student> students;

        private Student student;

        private Map<String, String> map;

        public static class Student {
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public String toString() {
            return "Teacher{" +
                    "name='" + name + '\'' +
                    ", price=" + price +
                    ", students=" + students +
                    ", student=" + student +
                    ", map=" + map +
                    '}';
        }
    }
}
