package com.kaikeba.springmvc.servlet;

import com.kaikeba.springmvc.adapter.HandlerAdapter;
import com.kaikeba.springmvc.adapter.support.HttpRequestHandlerAdapter;
import com.kaikeba.springmvc.adapter.support.SimpleControllerHandlerAdapter;
import com.kaikeba.springmvc.mapping.HandlerMapping;
import com.kaikeba.springmvc.mapping.support.BeanNameUrlHandlerMapping;
import com.kaikeba.springmvc.mapping.support.SimpleUrlHandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends AbstractServlet {

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        handlerAdapters.add(new HttpRequestHandlerAdapter());
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
        handlerMappings.add(new BeanNameUrlHandlerMapping());
        handlerMappings.add(new SimpleUrlHandlerMapping());
    }

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // 根据请求查找处理类
        Object handler = getHandler(request);
        if (handler == null) {
            return;
        }

        // 调用处理类的处理方法
        /*if (handler instanceof HttpRequestHandler) {
            ((HttpRequestHandler) handler).handleRequest(request, response);
        } else if (handler instanceof SimpleControllerHandler) {
            ((SimpleControllerHandler) handler).handleRequest(request, response);
        }*/

        // 先查找处理器适配器
        HandlerAdapter ha = getHandlerAdapter(handler);
        if (ha == null) {
            return;
        }
        ha.handleRequest(handler, request, response);

        // 响应结果

    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter ha : handlerAdapters) {
            if (ha.supports(handler)) {
                return ha;
            }
        }
        return null;
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping hm : handlerMappings) {
            Object handler = hm.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

}
