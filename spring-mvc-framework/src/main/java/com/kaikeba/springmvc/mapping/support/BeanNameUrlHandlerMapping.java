package com.kaikeba.springmvc.mapping.support;

import com.kaikeba.springmvc.handler.support.QueryUserHandler;
import com.kaikeba.springmvc.mapping.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:32:00
 */
public class BeanNameUrlHandlerMapping implements HandlerMapping {

    private Map<String, Object> urlHandlers = new HashMap<>();

    public BeanNameUrlHandlerMapping() {
        urlHandlers.put("/queryUser", new QueryUserHandler());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return urlHandlers.get(request.getRequestURI());
    }

}
