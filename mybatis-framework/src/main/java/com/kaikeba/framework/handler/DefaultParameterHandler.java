package com.kaikeba.framework.handler;

import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.ParameterMapping;
import com.kaikeba.util.SimpleTypeRegistry;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:28:37
 */
public class DefaultParameterHandler implements ParameterHandler {

    @Override
    public void setParameter(PreparedStatement preparedStatement, Object param, BoundSql boundSql) throws Exception {
        if (SimpleTypeRegistry.isSimpleType(param.getClass())) {
            preparedStatement.setObject(1, param);
        } else if (param instanceof Map) {
            Map map = (Map) param;

            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                // #{}中参数名称
                String name = parameterMapping.getName();
                Object value = map.get(name);
                preparedStatement.setObject(i + 1, value);
            }

        } else {
            // 无需实现
        }
    }

}
