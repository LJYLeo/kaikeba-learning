package com.kaikeba.dao;

import com.kaikeba.po.User;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 22:50:00
 */
public interface UserDao {

    /**
     * 查询用户列表
     *
     * @param param
     * @return
     */
    List<User> queryUserList(Map<String, Object> param);

}
