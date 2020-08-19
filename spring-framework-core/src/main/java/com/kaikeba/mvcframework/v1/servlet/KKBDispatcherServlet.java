package com.kaikeba.mvcframework.v1.servlet;

import com.kaikeba.mvcframework.annotation.KKBAutowired;
import com.kaikeba.mvcframework.annotation.KKBController;
import com.kaikeba.mvcframework.annotation.KKBRequestMapping;
import com.kaikeba.mvcframework.annotation.KKBService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 功能描述：
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:07:00
 */
public class KKBDispatcherServlet extends HttpServlet {

    private final Map<String, Object> mapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        InputStream is = null;
        Map<String, Object> extraMap = new HashMap<>();

        try {
            // 加载配置文件
            Properties configContext = new Properties();
            is = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
            configContext.load(is);

            // 扫描所有的类
            doScanner(configContext.getProperty("scanPackage"));

            for (String clazzName : mapping.keySet()) {
                if (!clazzName.contains(".")) {
                    continue;
                }
                Class<?> clazz = Class.forName(clazzName);
                // 处理controller
                if (clazz.isAnnotationPresent(KKBController.class)) {
                    mapping.put(clazzName, clazz.newInstance());
                    String baseUrl = "";
                    // 获取controller类上配置的根路径
                    if (clazz.isAnnotationPresent(KKBRequestMapping.class)) {
                        baseUrl = clazz.getAnnotation(KKBRequestMapping.class).value();
                    }
                    // 遍历方法，取出方法上配置路径
                    for (Method method : clazz.getMethods()) {
                        if (!method.isAnnotationPresent(KKBRequestMapping.class)) {
                            continue;
                        }
                        String url =
                                (baseUrl + "/" + method.getAnnotation(KKBRequestMapping.class).value()).replaceAll(
                                        "/+", "/");
                        extraMap.put(url, method);
                        System.out.println("Mapped " + url + "," + method);
                    }
                }
                // 处理service
                else if (clazz.isAnnotationPresent(KKBService.class)) {
                    // 获取beanName
                    String beanName = clazz.getAnnotation(KKBService.class).value();
                    if ("".equals(beanName)) {
                        beanName = clazz.getName();
                    }
                    // 将实例注入容器
                    Object instance = clazz.newInstance();
                    extraMap.put(beanName, instance);
                    // servie所有的接口都要额外注册一份，因为可能是通过接口来注入的
                    for (Class<?> i : clazz.getInterfaces()) {
                        extraMap.put(i.getName(), instance);
                    }
                }
            }
            mapping.putAll(extraMap);

            // 注入属性
            for (Object object : mapping.values()) {
                if (object == null) {
                    continue;
                }
                Class<?> clazz = object.getClass();
                if (clazz.isAnnotationPresent(KKBController.class)) {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(KKBAutowired.class)) {
                            continue;
                        }
                        String beanName = field.getAnnotation(KKBAutowired.class).value();
                        if ("".equals(beanName)) {
                            beanName = field.getType().getName();
                        }
                        field.setAccessible(true);
                        field.set(mapping.get(clazz.getName()), mapping.get(beanName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("KKB MVC Framework is inited");
    }

    private void doScanner(String scanPackage) throws Exception {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        if (url == null) {
            throw new Exception("scan package is not existed:" + scanPackage);
        }
        File classDir = new File(url.getFile());
        for (File file : Objects.requireNonNull(classDir.listFiles())) {
            // 是目录，递归扫描
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String clazzName = scanPackage + "." + file.getName().replace(".class", "");
                mapping.put(clazzName, null);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatcher(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 处理请求入参
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!mapping.containsKey(url)) {
            response.getWriter().write("404 Not Found!");
            return;
        }

        Method method = (Method) this.mapping.get(url);
        Map<String, String[]> parameterMap = request.getParameterMap();

        method.invoke(mapping.get(method.getDeclaringClass().getName()), request, response,
                parameterMap.get("name")[0]);
    }

}
