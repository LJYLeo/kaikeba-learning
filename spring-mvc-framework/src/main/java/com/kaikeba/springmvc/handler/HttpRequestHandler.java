package com.kaikeba.springmvc.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：处理类编写规范
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:02:00
 */
public interface HttpRequestHandler {

    void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
