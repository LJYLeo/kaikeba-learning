package com.kaikeba.test;

import com.kaikeba.po.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：基础版jdbc查询数据库
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-02 00:10:53
 */
public class JDBCDemo {

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        try {

            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 获取连接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8",
                    "root", "root");

            // 预编译sql
            preparedStatement = connection.prepareStatement("select * from user where id = ?");

            // 设置参数
            preparedStatement.setInt(1, 1);

            // 查询结果
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSex(resultSet.getString("sex"));
                user.setAddress(resultSet.getString("address"));
                user.setEmail(resultSet.getString("email"));

                userList.add(user);

            }

            System.out.println(userList);

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

    }

}
