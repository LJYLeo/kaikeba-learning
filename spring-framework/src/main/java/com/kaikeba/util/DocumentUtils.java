package com.kaikeba.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * 功能描述：文档处理工具类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 21:33:00
 */
public class DocumentUtils {

    public static Document createDocument(InputStream inputStream) {
        try {
            SAXReader saxReader = new SAXReader();
            return saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
