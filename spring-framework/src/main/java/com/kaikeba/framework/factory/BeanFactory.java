package com.kaikeba.framework.factory;

/**
 * 功能描述：bean工厂顶层接口，只暴露最简单的获取bean方法
 * 暗号：流星蝴蝶剑
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 16:31:00
 */
public interface BeanFactory {

    /**
     * 根据beanId获取bean实例
     *
     * @param name
     * @return
     */
    Object getBean(String name);

}
