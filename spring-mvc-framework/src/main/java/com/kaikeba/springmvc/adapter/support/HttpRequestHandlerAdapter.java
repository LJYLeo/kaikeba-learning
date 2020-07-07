package com.kaikeba.springmvc.adapter.support;

import com.kaikeba.springmvc.adapter.HandlerAdapter;
import com.kaikeba.springmvc.handler.HttpRequestHandler;
import com.kaikeba.springmvc.model.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:19:00
 */
public class HttpRequestHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HttpRequestHandler;
    }

    @Override
    public ModelAndView handleRequest(Object handler, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ((HttpRequestHandler) handler).handleRequest(request, response);
        return null;
    }

}
