package com.kaikeba.test;

import com.kaikeba.po.User;
import com.kaikeba.util.SimpleTypeRegistry;
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
public class MybatisV1 {

    private Properties properties = new Properties();

    @Test
    public void test() {

        loadProperties("jdbc.properties");

        // 入参普通属性
        List<User> userList = selectList("queryUserById", 1);
        System.out.println(userList);

        // 有多个参数的复杂入参
        userList = selectList("queryUserByCondition", new HashMap<String, Object>() {{
            put("name", "刘嘉宇");
            put("sex", "男");
        }});
        System.out.println(userList);

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
