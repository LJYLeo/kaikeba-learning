package com.kaikeba.framework.sqlnode.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.util.OgnlUtils;

/**
 * 功能描述：封装if标签的脚本信息
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:58:00
 */
public class IfSqlNode implements SqlNode {

    private String test;

    private SqlNode mixedSqlNode;

    public IfSqlNode(String test, SqlNode mixedSqlNode) {
        this.test = test;
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    public void apply(DynamicContext dynamicContext) {

        // 判断条件为true，才处理下面的sqlNode
        if (OgnlUtils.evaluateBoolean(test, dynamicContext.getBindings().get("_parameter"))) {
            mixedSqlNode.apply(dynamicContext);
        }

    }

}
