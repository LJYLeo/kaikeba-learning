package com.kaikeba.test;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.framework.sqlnode.support.MixedSqlNode;
import com.kaikeba.framework.sqlnode.support.TextSqlNode;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.framework.sqlsource.support.StaticSqlSource;
import com.kaikeba.po.User;
import com.kaikeba.util.SimpleTypeRegistry;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * 功能描述：手写mybatis第一版
 * 暗号：Java是最好的语言
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-02 00:42:51
 */
public class MybatisV2 {

    private Properties properties = new Properties();

    private Configuration configuration = new Configuration();

    private String namespace;

    @Test
    public void test() {

        loadXML("mybatis-config.xml");

        // 入参普通属性
        /*List<User> userList = selectList("test.findUserById", 1);
        System.out.println(userList);*/

    }

    private void loadXML(String s) {

        InputStream is = getResourceAsStream(s);
        Document document = createDocument(is);
        if (document != null) {
            parseConfiguration(document.getRootElement());
        }

    }

    private void parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        parseEnviroments(environments);
        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);
    }

    @SuppressWarnings("unchecked")
    private void parseMappers(Element mappers) {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            String resource = element.attributeValue("resource");
            InputStream mapperIs = getResourceAsStream(resource);
            Document document = createDocument(mapperIs);
            if (document != null) {
                parseMapper(document.getRootElement());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseMapper(Element rootElement) {
        namespace = rootElement.attributeValue("namespace");
        List<Element> selectElements = rootElement.elements("select");
        for (Element selectElement : selectElements) {
            parseStatementElement(selectElement);
        }
    }

    private void parseStatementElement(Element selectElement) {
        String statementId = selectElement.attributeValue("id");
        if (statementId == null || "".equals(statementId)) {
            return;
        }
        statementId = namespace + statementId;

        String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterClass = resolveType(parameterType);

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || "".equals(statementType) ? "prepared" : statementType;

        SqlSource sqlSource = createSqlSource(selectElement);

        MappedStatement mappedStatement = new MappedStatement(statementId, parameterClass, resultClass, statementType
                , sqlSource);

        configuration.addMappedStatement(statementId, mappedStatement);
    }

    private SqlSource createSqlSource(Element selectElement) {
        MixedSqlNode mixedSqlNode = parseDynamicTags(selectElement);
        return null;
    }

    private MixedSqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<>();
        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text) {
                String text = node.getText().trim();
                if ("".equals(text)) {
                    continue;
                }
                TextSqlNode textSqlNode = new TextSqlNode(text);
            } else if (node instanceof Element) {

            }
        }
        return new MixedSqlNode(sqlNodes);
    }

    private Class<?> resolveType(String className) {
        try {
            if (className != null) {
                return Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void parseEnviroments(Element environments) {
        String aDefault = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (id.equals(aDefault)) {
                Element dataSource = element.element("dataSource");
                parseDataSource(dataSource);
                break;
            }
        }
    }

    private void parseDataSource(Element dataSource) {
        String type = dataSource.attributeValue("type");
        if ("DBCP".equals(type)) {
            Properties properties = parseProperties(dataSource);
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(properties.getProperty("driver"));
            basicDataSource.setUrl(properties.getProperty("url"));
            basicDataSource.setUsername(properties.getProperty("username"));
            basicDataSource.setPassword(properties.getProperty("password"));
            configuration.setDataSource(basicDataSource);
        }
    }

    @SuppressWarnings("unchecked")
    private Properties parseProperties(Element dataSource) {
        Properties properties = new Properties();
        List<Element> propertyList = dataSource.elements("property");
        for (Element property : propertyList) {
            properties.put(property.attributeValue("name"), property.attributeValue("value"));
        }
        return properties;
    }

    private Document createDocument(InputStream is) {
        try {
            SAXReader saxReader = new SAXReader();
            return saxReader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getResourceAsStream(String s) {
        return this.getClass().getClassLoader().getResourceAsStream(s);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> selectList(String statementId, Object params) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> resultList = new ArrayList<>();

        try {

            // 加载驱动
            Class.forName(properties.getProperty("db.driver"));

            // 获取连接
            connection = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db"
                    + ".user"), properties.getProperty("db.password"));

            // 预编译sql
            preparedStatement = connection.prepareStatement(properties.getProperty("db.sql." + statementId));

            // 设置参数
            if (SimpleTypeRegistry.isSimpleType(params.getClass())) {
                // 暂时默认1个参数，直接设置值
                preparedStatement.setObject(1, params);
            } else if (params instanceof Map) {
                // 根据配置信息依次取参数值并注入preparedStatement
                Map paramsMap = (Map) params;
                String paramsText = properties.getProperty("db.sql." + statementId + ".params");
                String[] paramTextArray = paramsText.split(",");
                for (int i = 0; i < paramTextArray.length; i++) {
                    preparedStatement.setObject(i + 1, paramsMap.get(paramTextArray[i]));
                }
            } else {
                // 暂不实现
            }

            // 查询结果
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String className = properties.getProperty("db.sql." + statementId + ".returnType");
                Class<?> resultClass = Class.forName(className);
                T result = (T) resultClass.newInstance();
                for (Field field : resultClass.getDeclaredFields()) {
                    // 赋予权限，否则私有变量不好赋值
                    field.setAccessible(true);
                    field.set(result, resultSet.getObject(field.getName()));
                }

                resultList.add(result);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        return resultList;

    }

    private void loadProperties(String fileName) {
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(fileName);
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
