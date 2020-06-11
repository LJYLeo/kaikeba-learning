package com.kaikeba.framework.registry;

import com.kaikeba.ioc.BeanDefinition;

/**
 * 功能描述：beanDefinition注册器接口
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 17:03:00
 */
public interface BeanDefinitionRegistry {

    /**
     * 获取bean定义
     *
     * @param name
     * @return
     */
    BeanDefinition getBeanDefinition(String name);

    /**
     * 注册bean定义
     *
     * @param name
     * @param beanDefinition
     */
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

}
