package com.kaikeba.framework.sqlnode.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;

import java.util.List;

/**
 * 功能描述：封装同一级别的sqlNode
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:57:00
 */
public class MixedSqlNode implements SqlNode {

    private List<SqlNode> sqlNodes;

    public MixedSqlNode(List<SqlNode> sqlNodes) {
        this.sqlNodes = sqlNodes;
    }

    @Override
    public void apply(DynamicContext dynamicContext) {

        for (SqlNode sqlNode : sqlNodes) {
            sqlNode.apply(dynamicContext);
        }

    }

}
