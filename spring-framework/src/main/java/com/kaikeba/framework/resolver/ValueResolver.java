package com.kaikeba.framework.resolver;

import com.kaikeba.framework.factory.BeanFactory;
import com.kaikeba.ioc.RuntimeBeanReference;
import com.kaikeba.ioc.TypeStringValue;

/**
 * 功能描述：属性值处理辅助类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 19:06:00
 */
public class ValueResolver {

    private BeanFactory beanFactory;

    public ValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValue(Object value) {
        if (value instanceof RuntimeBeanReference) {
            // 引用类型直接取bean
            return beanFactory.getBean(((RuntimeBeanReference) value).getRef());
        } else if (value instanceof TypeStringValue) {
            // 简单类型是什么类型就转成什么类型
            TypeStringValue newValue = (TypeStringValue) value;
            if (newValue.getTargetType() == Integer.class) {
                return Integer.parseInt(newValue.getValue());
            } else if (newValue.getTargetType() == String.class) {
                return newValue.getValue();
            }
        }
        return null;
    }

}
