package com.kaikeba.springmvc.handler.support;

import com.kaikeba.springmvc.handler.SimpleControllerHandler;
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
 * @date 2020-06-24 11:09:00
 */
public class SaveUserHandler implements SimpleControllerHandler {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=utf8");
        response.getWriter().write("添加成功！！！");
        return null;
    }

}
