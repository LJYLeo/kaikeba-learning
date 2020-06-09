package com.kaikeba.ioc;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：bean描述类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:45:00
 */
@Data
public class BeanDefinition {

    private static final String SCOPE_SINGLETON = "singleton";
    private static final String SCOPE_PROTOTYPE = "prototype";

    private String beanName;

    private String initMethod;

    private String clazzName;

    private Class<?> clazzType;

    private String scope;

    private List<PropertyValue> propertyValues = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

}
