package com.kaikeba.framework.handler;

import com.kaikeba.framework.sqlsource.BoundSql;

import java.sql.PreparedStatement;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:25:12
 */
public interface ParameterHandler {

    void setParameter(PreparedStatement preparedStatement, Object param, BoundSql boundSql) throws Exception;

}
