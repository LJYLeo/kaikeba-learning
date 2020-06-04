package com.kaikeba.framework.sqlsource.support;

import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.ParameterMapping;
import com.kaikeba.framework.sqlsource.SqlSource;

import java.util.List;

/**
 * 功能描述：中间状态的SqlSource，存储其他sqlSource解析之后的结果
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:41:00
 */
public class StaticSqlSource implements SqlSource {

    private String sql;

    private List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public BoundSql getBoundSql(Object param) {
        return new BoundSql(sql, parameterMappings);
    }

}
