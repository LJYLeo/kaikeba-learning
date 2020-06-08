package com.kaikeba.framework.handler;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlsource.BoundSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:32:33
 */
public class PreparedStatementHandler implements StatementHandler {

    private ParameterHandler parameterHandler;
    private ResultSetHandler resultSetHandler;

    public PreparedStatementHandler(Configuration configuration) {
        this.parameterHandler = configuration.newParameterHandler();
        this.resultSetHandler = configuration.newResultSetHandler();
    }

    @Override
    public Statement prepare(Connection connection, String sql) throws Exception {
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement, Object param, BoundSql boundSql) throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        parameterHandler.setParameter(preparedStatement, param, boundSql);
    }

    @Override
    public <T> List<T> doQuery(Statement statement, MappedStatement mappedStatement) throws Exception {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> results = new ArrayList<>();
        resultSetHandler.handleResultSet(resultSet, results, mappedStatement);
        return results;
    }

}
