package com.kaikeba.framework.factory.reader;

import com.kaikeba.framework.registry.BeanDefinitionRegistry;
import com.kaikeba.util.DocumentUtils;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * 功能描述：将xml转换成beanDefinition
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 21:31:00
 */
public class XmlBeanDefinitionReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void registerBeanDefinition(InputStream inputStream) {
        Document document = DocumentUtils.createDocument(inputStream);
        if (document != null) {
            XmlBeanDefinitionDocumentReader xmlBeanDefinitionDocumentReader =
                    new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
            xmlBeanDefinitionDocumentReader.parseBeanDefinitions(document.getRootElement());
        }
    }

}
