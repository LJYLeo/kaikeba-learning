package com.kaikeba.framework.io;

import java.io.InputStream;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-08 10:32:06
 */
public class Resources {

    public static InputStream getResourceAsStream(String resource) {
        return Resources.class.getResourceAsStream(resource);
    }

}
