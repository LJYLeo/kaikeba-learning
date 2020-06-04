package com.kaikeba.framework.sqlsource.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.util.GenericTokenParser;
import com.kaikeba.util.ParameterMappingTokenHandler;

/**
 * 功能描述：封装并解析动态标签和${}相关的标签
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:38:00
 */
public class DynamicSqlSource implements SqlSource {

    private SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode rootSqlNode) {
        this.rootSqlNode = rootSqlNode;
    }

    @Override
    public BoundSql getBoundSql(Object param) {

        DynamicContext context = new DynamicContext(param);
        rootSqlNode.apply(context);

        String sqlText = context.getSql();

        ParameterMappingTokenHandler mappingTokenHandler = new ParameterMappingTokenHandler();
        String sql = new GenericTokenParser("#{", "}", mappingTokenHandler).parse(sqlText);

        return new BoundSql(sql, mappingTokenHandler.getParameterMappings());

    }

}
