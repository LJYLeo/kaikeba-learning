package com.kaikeba.framework.executor;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;

import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:22:44
 */
public interface Executor {

    <T> List<T> doQuery(Configuration configuration, MappedStatement mappedStatement, Object param);

}
