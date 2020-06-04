package com.kaikeba.framework.sqlnode.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;

/**
 * 功能描述：保存sql文本，不包含${}（#{}）
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:56:00
 */
public class StaticTextSqlNode implements SqlNode {

    private String sqlText;

    public StaticTextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    @Override
    public void apply(DynamicContext dynamicContext) {
        dynamicContext.appendSql(sqlText);
    }

}
