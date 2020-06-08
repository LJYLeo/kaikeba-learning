package com.kaikeba.framework.builder;

import com.kaikeba.framework.config.Configuration;
import com.kaikeba.framework.io.Resources;
import com.kaikeba.util.DocumentUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 功能描述：全局配置文件解析类
 * 暗号：班主任喜欢跳舞
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:08:51
 */
@SuppressWarnings("unchecked")
public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parseConfiguration(Element rootElement) {
        Element environments = rootElement.element("environments");
        parseEnviroments(environments);
        Element mappers = rootElement.element("mappers");
        parseMappers(mappers);
    }

    private void parseEnviroments(Element environments) {
        String aDefault = environments.attributeValue("default");
        List<Element> environmentList = environments.elements("environment");
        for (Element element : environmentList) {
            String id = element.attributeValue("id");
            if (id.equals(aDefault)) {
                Element dataSource = element.element("dataSource");
                parseDataSource(dataSource);
                break;
            }
        }
    }

    private void parseDataSource(Element dataSource) {
        String type = dataSource.attributeValue("type");
        if ("DBCP".equals(type)) {
            Properties properties = parseProperties(dataSource);
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDriverClassName(properties.getProperty("driver"));
            basicDataSource.setUrl(properties.getProperty("url"));
            basicDataSource.setUsername(properties.getProperty("username"));
            basicDataSource.setPassword(properties.getProperty("password"));
            configuration.setDataSource(basicDataSource);
        }
    }

    private Properties parseProperties(Element dataSource) {
        Properties properties = new Properties();
        List<Element> propertyList = dataSource.elements("property");
        for (Element property : propertyList) {
            properties.put(property.attributeValue("name"), property.attributeValue("value"));
        }
        return properties;
    }

    private void parseMappers(Element mappers) {
        List<Element> mapperList = mappers.elements("mapper");
        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(configuration);
        for (Element element : mapperList) {
            String resource = element.attributeValue("resource");
            InputStream mapperIs = Resources.getResourceAsStream(resource);
            Document document = DocumentUtils.createDocument(mapperIs);
            if (document != null) {
                mapperBuilder.parseMapper(document.getRootElement());
            }
        }
    }

}
