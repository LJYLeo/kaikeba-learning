package com.kaikeba.framework.builder;

import com.kaikeba.framework.config.Configuration;
import org.dom4j.Element;

import java.util.List;

/**
 * 功能描述：解析mapper文件
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:09:23
 */
public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    public void parseMapper(Element rootElement) {
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectElements = rootElement.elements("select");
        XMLStatementBuilder statementBuilder = new XMLStatementBuilder(configuration);
        for (Element selectElement : selectElements) {
            statementBuilder.parseStatementElement(namespace, selectElement);
        }
    }

}
