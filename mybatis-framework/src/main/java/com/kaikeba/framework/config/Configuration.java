package com.kaikeba.framework.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：全局配置类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 11:23:06
 */
public class Configuration {

    private DataSource dataSource;

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MappedStatement getMappedStatementById(String statementId) {
        return mappedStatements.get(statementId);
    }

    public void addMappedStatement(String statementId, MappedStatement mappedStatement) {
        this.mappedStatements.put(statementId, mappedStatement);
    }

}
