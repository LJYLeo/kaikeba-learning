package com.kaikeba.framework.executor;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.SqlSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:37:02
 */
public abstract class BaseExecutor implements Executor {

    private Map<String, List<Object>> oneLevelCache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> doQuery(Configuration configuration, MappedStatement mappedStatement, Object param) {
        SqlSource sqlSource = mappedStatement.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(param);
        String sql = boundSql.getSql();
        // 一级缓存处理
        List<Object> list = oneLevelCache.get(sql);
        if (list == null) {
            list = queryFromDataSource(configuration, mappedStatement, param, boundSql);
            oneLevelCache.put(sql, list);
        }

        return (List<T>) list;
    }

    public abstract <T> List<T> queryFromDataSource(Configuration configuration, MappedStatement mappedStatement,
                                                     Object param, BoundSql boundSql);

}
