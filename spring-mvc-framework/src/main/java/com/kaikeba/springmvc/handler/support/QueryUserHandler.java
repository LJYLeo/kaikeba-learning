package com.kaikeba.springmvc.handler.support;

import com.kaikeba.springmvc.handler.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:06:00
 */
public class QueryUserHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        response.setContentType("text/plain;charset=utf8");
        response.getWriter().write("id:" + id + "----name:" + name);
    }

}
