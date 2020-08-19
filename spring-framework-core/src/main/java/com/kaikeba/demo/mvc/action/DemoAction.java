package com.kaikeba.demo.mvc.action;

import com.kaikeba.demo.service.IDemoService;
import com.kaikeba.mvcframework.annotation.KKBAutowired;
import com.kaikeba.mvcframework.annotation.KKBController;
import com.kaikeba.mvcframework.annotation.KKBRequestMapping;
import com.kaikeba.mvcframework.annotation.KKBRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:59:00
 */
@KKBController
@KKBRequestMapping
public class DemoAction {

    @KKBAutowired
    private IDemoService demoService;

    @KKBRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @KKBRequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @KKBRequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response, @KKBRequestParam("a") Integer a,
                    @KKBRequestParam("b") Integer b) {
        try {
            response.getWriter().write(a + "+" + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @KKBRequestMapping("/remove")
    public void remove(HttpServletRequest request, HttpServletResponse response, @KKBRequestParam("id") Integer id) {

    }

}
