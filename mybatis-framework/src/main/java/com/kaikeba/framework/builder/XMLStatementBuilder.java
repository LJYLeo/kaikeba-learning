package com.kaikeba.framework.builder;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.config.MappedStatement;
import com.kaikeba.framework.sqlsource.SqlSource;
import com.kaikeba.util.ReflectUtils;
import org.dom4j.Element;

/**
 * 功能描述：解析statement节点
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:10:17
 */
public class XMLStatementBuilder {

    private Configuration configuration;

    public XMLStatementBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseStatementElement(String namespace, Element selectElement) {
        String statementId = selectElement.attributeValue("id");
        if (statementId == null || "".equals(statementId)) {
            return;
        }
        statementId = namespace + "." + statementId;

        String parameterType = selectElement.attributeValue("parameterType");
        Class<?> parameterClass = ReflectUtils.resolveType(parameterType);

        String resultType = selectElement.attributeValue("resultType");
        Class<?> resultClass = ReflectUtils.resolveType(resultType);

        String statementType = selectElement.attributeValue("statementType");
        statementType = statementType == null || "".equals(statementType) ? "prepared" : statementType;

        SqlSource sqlSource = createSqlSource(selectElement);

        MappedStatement mappedStatement = new MappedStatement(statementId, parameterClass, resultClass, statementType
                , sqlSource);

        configuration.addMappedStatement(statementId, mappedStatement);
    }

    private SqlSource createSqlSource(Element selectElement) {
        XMLScriptBuilder xmlScriptBuilder = new XMLScriptBuilder();
        return xmlScriptBuilder.parseScriptNode(selectElement);
    }

}
