package com.kaikeba.framework.handler;

import com.kaikeba.framework.config.MappedStatement;

import java.sql.ResultSet;
import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:27:40
 */
public interface ResultSetHandler {

    <T> void handleResultSet(ResultSet resultSet, List<T> results, MappedStatement mappedStatement) throws Exception;

}
