package com.kaikeba.framework.sqlsource.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.framework.sqlsource.BoundSql;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.util.GenericTokenParser;
import com.kaikeba.util.ParameterMappingTokenHandler;

/**
 * 功能描述：封装并解析非动态标签和非${}相关的标签（#{}）
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:40:00
 */
public class RawSqlSource implements SqlSource {

    private SqlSource sqlSource;

    public RawSqlSource(SqlNode rootSqlNode) {

        DynamicContext context = new DynamicContext(null);
        rootSqlNode.apply(context);

        String sqlText = context.getSql();

        ParameterMappingTokenHandler mappingTokenHandler = new ParameterMappingTokenHandler();
        String sql = new GenericTokenParser("#{", "}", mappingTokenHandler).parse(sqlText);

        sqlSource = new StaticSqlSource(sql, mappingTokenHandler.getParameterMappings());

    }

    @Override
    public BoundSql getBoundSql(Object param) {
        return sqlSource.getBoundSql(param);
    }

}
