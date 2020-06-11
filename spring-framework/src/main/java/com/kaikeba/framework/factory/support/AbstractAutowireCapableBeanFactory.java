package com.kaikeba.framework.factory.support;

import com.kaikeba.framework.resolver.ValueResolver;
import com.kaikeba.ioc.BeanDefinition;
import com.kaikeba.ioc.PropertyValue;
import com.kaikeba.util.ReflectionUtils;

import java.util.List;

/**
 * 功能描述：beanFactory的实现类，完成bean装配功能，但是其他的功能还是延迟到子类去实现
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 17:35:00
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(BeanDefinition beanDefinition) {
        // 实例化bean
        Object bean = createBeanInstance(beanDefinition);
        if (bean == null) {
            return null;
        }

        // 注入
        populateBean(bean, beanDefinition);

        // 调用初始化方法
        invokeInitMethod(bean, beanDefinition);

        return bean;
    }

    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) {
        try {
            String initMethod = beanDefinition.getInitMethod();
            if (initMethod == null) {
                return;
            }
            ReflectionUtils.invokeMethod(bean, initMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues != null && propertyValues.size() > 0) {
            for (PropertyValue propertyValue : propertyValues) {
                String name = propertyValue.getName();
                // 原始值
                Object value = propertyValue.getValue();
                ValueResolver valueResolver = new ValueResolver(this);
                // 处理后的值
                Object value2Use = valueResolver.resolveValue(value);
                // 注入值
                ReflectionUtils.setProperty(bean, name, value2Use);
            }
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return ReflectionUtils.createInstance(beanDefinition.getClazzType());
    }

}
