package com.kaikeba.test;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.framework.sqlnode.support.IfSqlNode;
import com.kaikeba.framework.sqlnode.support.MixedSqlNode;
import com.kaikeba.framework.sqlnode.support.StaticTextSqlNode;
import com.kaikeba.framework.sqlnode.support.TextSqlNode;
import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.ParameterMapping;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.framework.sqlsource.support.DynamicSqlSource;
import com.kaikeba.framework.sqlsource.support.RawSqlSource;
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

    private Configuration configuration = new Configuration();

    private String namespace;

    private boolean isDynamic;

    @Test
    public void test() {

        loadXML("mybatis-config.xml");

        // 入参普通属性
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("id", 1);
            put("username", "t");
        }};
        List<User> userList = selectList("test.findUserById", param);
        System.out.println(userList);

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
        statementId = namespace + "." + statementId;

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
        SqlSource sqlSource;
        if (isDynamic) {
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        } else {
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
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
                if (textSqlNode.isDynamic()) {
                    isDynamic = true;
                    sqlNodes.add(textSqlNode);
                } else {
                    sqlNodes.add(new StaticTextSqlNode(text));
                }
            } else if (node instanceof Element) {
                isDynamic = true;
                Element element = (Element) node;
                String name = element.getName();
                if ("if".equals(name)) {
                    String test = element.attributeValue("test");
                    MixedSqlNode mixedSqlNode = parseDynamicTags(element);
                    sqlNodes.add(new IfSqlNode(test, mixedSqlNode));
                } else if ("where".equals(name)) {
                    // 暂不实现
                }
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

    private <T> List<T> selectList(String statementId, Object param) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<T> results = new ArrayList<>();

        try {

            MappedStatement mappedStatement = configuration.getMappedStatementById(statementId);
            connection = getConnection();

            BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(param);
            String sql = boundSql.getSql();
            System.out.println(sql);

            String statementType = mappedStatement.getStatementType();
            if (connection != null) {
                if ("prepared".equals(statementType)) {
                    preparedStatement = connection.prepareStatement(sql);
                    setParameters(preparedStatement, param, boundSql);
                    rs = preparedStatement.executeQuery();
                    handleResultSet(rs, results, mappedStatement.getResultTypeClass());
                } else {
                    // 暂不实现
                }
            }

            return results;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
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

        return null;

    }

    @SuppressWarnings("unchecked")
    private <T> void handleResultSet(ResultSet rs, List<T> results, Class<?> clazz) throws Exception {
        while (rs.next()) {
            T result = (T) clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                // 赋予权限，否则私有变量不好赋值
                field.setAccessible(true);
                field.set(result, rs.getObject(field.getName()));
            }
            results.add(result);
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object param, BoundSql boundSql) throws Exception {
        if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
            preparedStatement.setObject(1, param);
        } else if (param instanceof Map) {
            Map map = (Map) param;
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                preparedStatement.setObject(i + 1, map.get(parameterMapping.getName()));
            }
        } else {
            // 暂不实现
        }
    }

    private Connection getConnection() {
        try {
            return configuration.getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
