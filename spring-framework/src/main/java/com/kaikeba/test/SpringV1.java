package com.kaikeba.test;

import com.kaikeba.dao.UserDaoImpl;
import com.kaikeba.po.User;
import com.kaikeba.service.UserServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:05:00
 */
public class SpringV1 {

    @Test
    public void test() {

        // 创建查询对象
        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();

        // 创建依赖对象，此处为数据源
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        // 注入
        userDao.setDataSource(dataSource);
        userService.setUserDao(userDao);

        // 查询结果
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("username", "test");
        }};
        List<User> resultList = userService.queryUsers(param);
        System.out.println(resultList);

    }

}
