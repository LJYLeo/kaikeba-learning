package com.kaikeba.test;

import com.kaikeba.framework.factory.reader.XmlBeanDefinitionReader;
import com.kaikeba.framework.factory.support.DefaultListableBeanFactory;
import com.kaikeba.framework.resource.Resource;
import com.kaikeba.framework.resource.support.ClassPathResource;
import com.kaikeba.po.User;
import com.kaikeba.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 17:10:00
 */
public class SpringV3 {

    private DefaultListableBeanFactory beanFactory;

    @Before
    public void before() {
        Resource resource = new ClassPathResource("beans.xml");
        InputStream inputStream = resource.getResource();

        beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        xmlBeanDefinitionReader.registerBeanDefinition(inputStream);
    }

    @Test
    public void test() {

        // 取出想要的bean
        UserService userService = (UserService) beanFactory.getBean("userService");

        // 查询结果
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("username", "test");
        }};
        List<User> resultList = userService.queryUsers(param);
        System.out.println(resultList);

    }

}
