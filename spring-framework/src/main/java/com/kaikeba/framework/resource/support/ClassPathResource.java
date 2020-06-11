package com.kaikeba.framework.resource.support;

import com.kaikeba.framework.resource.Resource;

import java.io.InputStream;

/**
 * 功能描述：从项目resource文件夹获取资源
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-11 21:29:00
 */
public class ClassPathResource implements Resource {

    private String path;

    public ClassPathResource(String path) {
        this.path = path;
    }

    @Override
    public InputStream getResource() {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

}
