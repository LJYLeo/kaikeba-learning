package com.kaikeba.framework.factory.support;

import com.kaikeba.framework.registry.BeanDefinitionRegistry;
import com.kaikeba.ioc.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：Spring真正使用的bean工厂
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 19:13:00
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    /**
     * 其他bean容器
     */
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitions.get(name);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitions.put(name, beanDefinition);
    }

}
