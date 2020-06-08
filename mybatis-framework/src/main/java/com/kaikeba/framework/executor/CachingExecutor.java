package com.kaikeba.framework.executor;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;

import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 11:43:07
 */
public class CachingExecutor implements Executor {

    private Executor delegate;

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> List<T> doQuery(Configuration configuration, MappedStatement mappedStatement, Object param) {
        return delegate.doQuery(configuration, mappedStatement, param);
    }

}
