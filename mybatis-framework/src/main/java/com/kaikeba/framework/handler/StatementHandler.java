package com.kaikeba.framework.handler;

import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlsource.BoundSql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:26:15
 */
public interface StatementHandler {

    Statement prepare(Connection connection, String sql) throws Exception;

    void parameterize(Statement statement, Object param, BoundSql boundSql) throws Exception;

    <T> List<T> doQuery(Statement statement, MappedStatement mappedStatement) throws Exception;

}
