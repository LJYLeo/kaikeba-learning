package com.kaikeba.springmvc.adapter;

import com.kaikeba.springmvc.model.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：处理器适配器
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:17:00
 */
public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handleRequest(Object handler, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
