package com.kaikeba.framework.factory.reader;

import com.kaikeba.framework.registry.BeanDefinitionRegistry;
import com.kaikeba.ioc.BeanDefinition;
import com.kaikeba.ioc.PropertyValue;
import com.kaikeba.ioc.RuntimeBeanReference;
import com.kaikeba.ioc.TypeStringValue;
import com.kaikeba.util.ReflectionUtils;
import org.dom4j.Element;

import java.util.List;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 21:35:00
 */
public class XmlBeanDefinitionDocumentReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionDocumentReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void parseBeanDefinitions(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            // 解析bean标签
            if ("bean".equals(element.getName())) {
                parseBeanElement(element);
            } else {
                // 解析其他自定义标签
                parseCustomElement(element);
            }
        }
    }

    private void parseCustomElement(Element element) {
        // 暂不实现
    }

    private void parseBeanElement(Element element) {

        try {
            String id = element.attributeValue("id");
            String name = element.attributeValue("name");
            String initMethod = element.attributeValue("init-method");
            String clazzName = element.attributeValue("class");
            String scope = element.attributeValue("scope");

            scope = scope == null || "".equals(scope) ? "singleton" : scope;
            String beanName = id == null ? name : id;
            Class<?> clazzType = Class.forName(clazzName);

            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanName(beanName);
            beanDefinition.setInitMethod(initMethod);
            beanDefinition.setClazzName(clazzName);
            beanDefinition.setClazzType(clazzType);
            beanDefinition.setScope(scope);

            List<Element> propertyElements = element.elements("property");
            for (Element propertyElement : propertyElements) {
                parsePropertyElement(beanDefinition, propertyElement);
            }

            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parsePropertyElement(BeanDefinition beanDefinition, Element propertyElement) throws Exception {
        if (propertyElement == null) {
            return;
        }
        String name = propertyElement.attributeValue("name");
        String value = propertyElement.attributeValue("value");
        String ref = propertyElement.attributeValue("ref");
        // value和ref不能同时存在
        if (value != null && !"".equals(value) && ref != null && !"".equals(ref)) {
            return;
        }

        PropertyValue propertyValue = null;
        // value有值，创建简单类型的property
        if (value != null && !"".equals(value)) {
            Class<?> targetType = ReflectionUtils.getTypeByFieldName(beanDefinition.getClazzType(), name);
            TypeStringValue typeStringValue = new TypeStringValue();
            typeStringValue.setValue(value);
            typeStringValue.setTargetType(targetType);
            propertyValue = new PropertyValue(name, typeStringValue);
        } else if (ref != null && !"".equals(ref)) {
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(ref);
            propertyValue = new PropertyValue(name, runtimeBeanReference);
        }
        beanDefinition.addPropertyValue(propertyValue);
    }

}
