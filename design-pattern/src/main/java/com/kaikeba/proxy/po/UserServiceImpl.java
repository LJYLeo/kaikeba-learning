package com.kaikeba.proxy.po;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-18 12:42:00
 */
public class UserServiceImpl implements UserService {

    @Override
    public void saveUser(String name) {
        System.out.println("添加用户：" + name);
    }

}
