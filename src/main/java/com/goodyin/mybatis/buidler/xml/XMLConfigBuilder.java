package com.goodyin.mybatis.buidler.xml;

import com.goodyin.mybatis.buidler.BaseBuilder;
import com.goodyin.mybatis.datasource.druid.DruidDataSourceFactory;
import com.goodyin.mybatis.io.Resources;
import com.goodyin.mybatis.mapping.BoundSql;
import com.goodyin.mybatis.mapping.Environment;
import com.goodyin.mybatis.mapping.MappedStatement;
import com.goodyin.mybatis.mapping.SqlCommandType;
import com.goodyin.mybatis.session.Configuration;
import com.goodyin.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML配置构建器
 */
public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j(Java的XML API) 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置，类型别名、插件、对象工厂、对象包装工厂、设置、类型转换、映射器
     * @return
     */
    public Configuration parse() {
        try {
            // 解析数据库连接环境
            environmentsElement(root.element("environments"));
            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configuration;

    }

    /**
     * 解析环境参数
     * @param context
     */
    private void environmentsElement(Element context) throws InstantiationException, IllegalAccessException {
        String environment = context.attributeValue("default");

        List<Element> environmentList = context.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (environment.equals(id)) {
                // 事务管理器
                Element transactionManager = element.element("transactionManager");
                TransactionFactory transactionFactory = (TransactionFactory)typeAliasRegistry.resolveAlias(transactionManager.attributeValue("type")).newInstance();

                // 数据源
                Element datasourceElement = element.element("dataSource");
                DruidDataSourceFactory dataSourceFactory = (DruidDataSourceFactory) typeAliasRegistry.resolveAlias(datasourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = datasourceElement.elements("property");
                Properties properties = new Properties();
                for (Element property : propertyList) {
                    properties.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(properties);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(transactionFactory)
                        .dataSource(dataSource);
                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    /**
     * 解析映射器
     * @param mappers
     * @throws IOException
     * @throws DocumentException
     */
    private void mapperElement(Element mappers) throws IOException, DocumentException, ClassNotFoundException {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            // 命名空间
            String namespace = root.attributeValue("namespace");

            // SELECT
            List<Element> selectNodes = root.elements("select");
            for (Element selectNode : selectNodes) {
                String id = selectNode.attributeValue("id");
                String parameterType = selectNode.attributeValue("parameterType");
                String resultType = selectNode.attributeValue("resultType");
                String sql = selectNode.getText();

                // 正则匹配 用?替换#{}
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 0; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }
                String msId = namespace + "." + id;
                String nodeName = selectNode.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ROOT));

                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);

                MappedStatement mappedStatement = new MappedStatement
                        .Builder(configuration, msId, sqlCommandType, boundSql)
                        .build();

                // 添加解析SQL
                configuration.addMappedStatement(mappedStatement);
            }
            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));

        }


    }
}
