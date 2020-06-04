package com.kaikeba.framework.sqlsource;

import java.util.List;

/**
 * 功能描述：sql封装对象
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 11:50:42
 */
public class BoundSql {

    /**
     * 可执行sql
     */
    private String sql;

    /**
     * sql中的参数类型映射
     */
    private List<ParameterMapping> parameterMappings;

    public BoundSql(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void addParameterMapping(ParameterMapping parameterMapping) {
        this.parameterMappings.add(parameterMapping);
    }

}
