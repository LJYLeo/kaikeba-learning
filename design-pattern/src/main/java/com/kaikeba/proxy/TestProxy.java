package com.kaikeba.proxy;

import com.kaikeba.proxy.factory.CglibProxyFactory;
import com.kaikeba.proxy.factory.JDKProxyFactory;
import com.kaikeba.proxy.po.UserService;
import com.kaikeba.proxy.po.UserServiceImpl;
import org.junit.Test;

/**
 * 功能描述：测试类
 * 暗号：倚天屠龙记
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-18 12:40:00
 */
public class TestProxy {

    @Test
    public void testJdkProxy() {

        UserService proxy = (UserService) JDKProxyFactory.getProxy(new UserServiceImpl());
        proxy.saveUser("test1");

    }

    @Test
    public void testCglibProxy() {
        UserService proxy = (UserService) CglibProxyFactory.getProxy(new UserServiceImpl());
        proxy.saveUser("test2");
    }

}
