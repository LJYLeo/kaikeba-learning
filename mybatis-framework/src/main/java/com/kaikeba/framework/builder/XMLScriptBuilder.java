package com.kaikeba.framework.builder;

import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.framework.sqlnode.support.IfSqlNode;
import com.kaikeba.framework.sqlnode.support.MixedSqlNode;
import com.kaikeba.framework.sqlnode.support.StaticTextSqlNode;
import com.kaikeba.framework.sqlnode.support.TextSqlNode;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.framework.sqlsource.support.DynamicSqlSource;
import com.kaikeba.framework.sqlsource.support.RawSqlSource;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：解析sql语句节点
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:09:49
 */
public class XMLScriptBuilder {

    private boolean isDynamic;

    public SqlSource parseScriptNode(Element selectElement) {
        MixedSqlNode mixedSqlNode = parseDynamicTags(selectElement);
        SqlSource sqlSource;
        if (isDynamic) {
            sqlSource = new DynamicSqlSource(mixedSqlNode);
        } else {
            sqlSource = new RawSqlSource(mixedSqlNode);
        }
        return sqlSource;
    }

    private MixedSqlNode parseDynamicTags(Element selectElement) {
        List<SqlNode> sqlNodes = new ArrayList<>();
        int nodeCount = selectElement.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = selectElement.node(i);
            if (node instanceof Text) {
                String text = node.getText().trim();
                if ("".equals(text)) {
                    continue;
                }
                TextSqlNode textSqlNode = new TextSqlNode(text);
                if (textSqlNode.isDynamic()) {
                    isDynamic = true;
                    sqlNodes.add(textSqlNode);
                } else {
                    sqlNodes.add(new StaticTextSqlNode(text));
                }
            } else if (node instanceof Element) {
                isDynamic = true;
                Element element = (Element) node;
                String name = element.getName();
                if ("if".equals(name)) {
                    String test = element.attributeValue("test");
                    MixedSqlNode mixedSqlNode = parseDynamicTags(element);
                    sqlNodes.add(new IfSqlNode(test, mixedSqlNode));
                } else if ("where".equals(name)) {
                    // 暂不实现
                }
            }
        }
        return new MixedSqlNode(sqlNodes);
    }

}
