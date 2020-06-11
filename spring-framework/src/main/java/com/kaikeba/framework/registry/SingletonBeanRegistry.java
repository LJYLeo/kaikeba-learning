package com.kaikeba.framework.registry;

/**
 * 功能描述：单例bean注册器接口
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 16:57:00
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例bean
     *
     * @param name
     * @return
     */
    Object getSingleton(String name);

    /**
     * 将单例bean添加至容器中
     *
     * @param name
     * @param bean
     */
    void addSingleton(String name, Object bean);

}
