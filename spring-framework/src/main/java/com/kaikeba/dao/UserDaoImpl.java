package com.kaikeba.dao;

import com.kaikeba.po.User;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 22:51:00
 */
public class UserDaoImpl implements UserDao {

    private DataSource dataSource;

    @Override
    public List<User> queryUserList(Map<String, Object> param) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<User> results = new ArrayList<>();

        try {

            connection = dataSource.getConnection();

            String sql = "select * from user where name = ?";

            // 预编译sql
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            preparedStatement.setObject(1, param.get("username"));

            // 查询结果
            rs = preparedStatement.executeQuery();

            Class<?> clazz = User.class;
            while (rs.next()) {
                User result = (User) clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    // 赋予权限，否则私有变量不好赋值
                    field.setAccessible(true);
                    field.set(result, rs.getObject(field.getName()));
                }
                results.add(result);
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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
