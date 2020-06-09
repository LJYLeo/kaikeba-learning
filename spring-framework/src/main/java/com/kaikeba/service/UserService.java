package com.kaikeba.service;

import com.kaikeba.po.User;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:03:00
 */
public interface UserService {

    /**
     * 查询用户
     *
     * @param param
     * @return
     */
    List<User> queryUsers(Map<String, Object> param);

}
