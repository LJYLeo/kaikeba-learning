package com.kaikeba.proxy.factory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-18 16:49:00
 */
public class CglibProxyFactory {

    public static Object getProxy(Object target) {

        // 创建增强器
        Enhancer enhancer = new Enhancer();
        // 注入目标类
        enhancer.setSuperclass(target.getClass());
        // 增强代码
        enhancer.setCallback(new MyMethodInterceptor());
        return enhancer.create();

    }

    static class MyMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("cglib动态代理开始...");
            Object result = methodProxy.invokeSuper(proxy, args);
            System.out.println("cglib动态代理结束...");
            return result;
        }

    }

}
