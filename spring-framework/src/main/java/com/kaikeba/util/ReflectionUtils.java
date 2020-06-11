package com.kaikeba.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 功能描述：反射工具类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 19:00:00
 */
public class ReflectionUtils {

    /**
     * 创建实例
     *
     * @param clazzType
     * @return
     */
    public static Object createInstance(Class<?> clazzType) {
        try {
            return clazzType.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setProperty(Object bean, String name, Object value2Use) {
        try {
            Field declaredField = bean.getClass().getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(bean, value2Use);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void invokeMethod(Object bean, String methodName) {
        try {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getTypeByFieldName(Class<?> clazzType, String name) throws Exception {
        Field declaredField = clazzType.getDeclaredField(name);
        return declaredField.getType();
    }

}
