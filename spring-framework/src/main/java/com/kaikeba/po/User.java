package com.kaikeba.po;

import lombok.Data;

/**
 * 功能描述：用户类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-01 23:58:20
 */
@Data
public class User {

    /**
     * 自增id
     */
    private long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮箱
     */
    private String email;

}
