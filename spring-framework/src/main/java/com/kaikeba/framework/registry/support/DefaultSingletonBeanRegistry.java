package com.kaikeba.framework.registry.support;

import com.kaikeba.framework.registry.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：单例bean注册器默认实现类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 16:59:00
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 单例bean容器
     */
    private Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }

    @Override
    public void addSingleton(String name, Object bean) {
        singletonObjects.put(name, bean);
    }

}
