package com.kaikeba.framework.factory.support;

import com.kaikeba.framework.factory.BeanFactory;
import com.kaikeba.framework.registry.support.DefaultSingletonBeanRegistry;
import com.kaikeba.ioc.BeanDefinition;

/**
 * 功能描述：抽象bean工厂，同时也是一个单例bean注册器，实现获取bean接口
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 16:50:00
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) {
        Object bean = getSingleton(name);

        if (bean != null) {
            return bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            return null;
        }

        if (beanDefinition.isSingleton()) {
            bean = createBean(beanDefinition);
            addSingleton(name, bean);
        } else if (beanDefinition.isPrototype()) {
            bean = createBean(beanDefinition);
        } else {
            // 无需实现
        }

        return bean;
    }

    protected abstract BeanDefinition getBeanDefinition(String name);

    protected abstract Object createBean(BeanDefinition beanDefinition);

}
