package com.kaikeba.service;

import com.kaikeba.dao.UserDao;
import com.kaikeba.po.User;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:04:00
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public List<User> queryUsers(Map<String, Object> param) {
        return userDao.queryUserList(param);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
