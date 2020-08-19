package com.kaikeba.demo.service.impl;

import com.kaikeba.demo.service.IDemoService;
import com.kaikeba.mvcframework.annotation.KKBService;

/**
 * 功能描述：
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:58:00
 */
@KKBService
public class DemoService implements IDemoService {

    @Override
    public String get(String name) {
        return "My name is " + name;
    }

}
