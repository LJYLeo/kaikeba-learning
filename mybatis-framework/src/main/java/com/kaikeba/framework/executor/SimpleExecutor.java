package com.kaikeba.framework.executor;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.handler.StatementHandler;
import com.kaikeba.framework.sqlsource.BoundSql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:36:42
 */
public class SimpleExecutor extends BaseExecutor {

    @Override
    public <T> List<T> queryFromDataSource(Configuration configuration, MappedStatement mappedStatement, Object param
            , BoundSql boundSql) {
        Connection connection;
        List<T> results;

        try {
            // 获取连接
            connection = getConnection(configuration);

            StatementHandler statementHandler = configuration.newStatementHandler(mappedStatement.getStatementType());
            Statement statement = statementHandler.prepare(connection, boundSql.getSql());
            statementHandler.parameterize(statement, param, boundSql);
            results = statementHandler.doQuery(statement, mappedStatement);

            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Connection getConnection(Configuration configuration) {
        try {
            return configuration.getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
