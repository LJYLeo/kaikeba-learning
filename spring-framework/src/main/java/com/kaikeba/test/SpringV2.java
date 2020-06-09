package com.kaikeba.test;

import com.kaikeba.ioc.BeanDefinition;
import com.kaikeba.ioc.PropertyValue;
import com.kaikeba.ioc.RuntimeBeanReference;
import com.kaikeba.ioc.TypeStringValue;
import com.kaikeba.po.User;
import com.kaikeba.service.UserService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:11:00
 */
@SuppressWarnings("unchecked")
public class SpringV2 {

    /**
     * 单例bean容器
     */
    private Map<String, Object> singletonDefinitions = new HashMap<>();

    /**
     * 其他bean容器
     */
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Test
    public void test() {

        // 解析bean信息并将其放入容器中
        registerBeanDefinitions();

        // 取出想要的bean
        UserService userService = (UserService) getBean("userService");

        // 查询结果
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("username", "test");
        }};
        List<User> resultList = userService.queryUsers(param);
        System.out.println(resultList);

    }

    private Object getBean(String beanName) {
        Object bean = this.singletonDefinitions.get(beanName);

        if (bean != null) {
            return bean;
        }

        BeanDefinition beanDefinition = this.beanDefinitions.get(beanName);
        if (beanDefinition == null) {
            return null;
        }

        if (beanDefinition.isSingleton()) {
            bean = createBean(beanDefinition);
            this.singletonDefinitions.put(beanName, bean);
        } else if (beanDefinition.isPrototype()) {
            bean = createBean(beanDefinition);
        } else {
            // 无需实现
        }

        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        return null;
    }

    private void registerBeanDefinitions() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("beans.xml");
        Document document = createDocument(is);
        if (document != null) {
            parseBeanDefinitions(document.getRootElement());
        }
    }

    private void parseBeanDefinitions(Element rootElement) {
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

            this.beanDefinitions.put(beanName, beanDefinition);
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
            Class<?> targetType = getTypeByFieldName(beanDefinition.getClazzType(), name);
            TypeStringValue typeStringValue = new TypeStringValue();
            typeStringValue.setName(name);
            typeStringValue.setTargetType(targetType);
            propertyValue = new PropertyValue(name, typeStringValue);
        } else if (ref != null && !"".equals(ref)) {
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(ref);
            propertyValue = new PropertyValue(name, runtimeBeanReference);
        }
        beanDefinition.addPropertyValue(propertyValue);
    }

    private Class<?> getTypeByFieldName(Class<?> clazzType, String name) throws Exception {
        Field declaredField = clazzType.getDeclaredField(name);
        return declaredField.getType();
    }

    private void parseCustomElement(Element element) {
        // 暂不实现
    }

    private Document createDocument(InputStream is) {
        try {
            SAXReader saxReader = new SAXReader();
            return saxReader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
