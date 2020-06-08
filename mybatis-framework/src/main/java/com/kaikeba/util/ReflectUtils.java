package com.kaikeba.util;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:16:12
 */
public class ReflectUtils {

    public static Class<?> resolveType(String className) {
        try {
            if (className != null) {
                return Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
