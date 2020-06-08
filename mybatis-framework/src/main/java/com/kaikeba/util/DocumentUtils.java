package com.kaikeba.util;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:33:55
 */
public class DocumentUtils {

    public static Document createDocument(InputStream inputStream) {
        try {
            SAXReader reader = new SAXReader();
            return reader.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
