package com.kaikeba.framework.handler;

import com.kaikeba.framework.config.MappedStatement;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:30:00
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    @Override
    @SuppressWarnings("unchecked")
    public <T> void handleResultSet(ResultSet resultSet, List<T> results, MappedStatement mappedStatement) throws Exception {
        Class<?> resultTypeClass = mappedStatement.getResultTypeClass();
        while (resultSet.next()) {
            T result = (T) resultTypeClass.newInstance();
            for (Field field : resultTypeClass.getDeclaredFields()) {
                // 赋予权限，否则私有变量不好赋值
                field.setAccessible(true);
                field.set(result, resultSet.getObject(field.getName()));
            }
            results.add(result);
        }
    }

}
