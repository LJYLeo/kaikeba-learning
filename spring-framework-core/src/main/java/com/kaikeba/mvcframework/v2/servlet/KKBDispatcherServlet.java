package com.kaikeba.mvcframework.v2.servlet;

import com.kaikeba.mvcframework.annotation.*;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
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

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 扫描相关类
        doScanner(contextConfig.getProperty("scanPackage"));

        // 初始化扫描到的类，放到IOC容器中
        doInstance();

        // 完成注入
        doAutowired();

        // 初始化handlerMapping
        initHandlerMapping();

        System.out.println("KKB MVC Framework is inited");

    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(KKBController.class)) {
                continue;
            }
            String baseUrl = "";
            if (clazz.isAnnotationPresent(KKBRequestMapping.class)) {
                baseUrl = clazz.getAnnotation(KKBRequestMapping.class).value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(KKBRequestMapping.class)) {
                    continue;
                }
                String url =
                        ("/" + baseUrl + "/" + method.getAnnotation(KKBRequestMapping.class).value()).replaceAll("/+"
                                , "/");
                handlerMapping.put(url, method);
                System.out.println("Mapped :" + url + "," + method);
            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(KKBAutowired.class)) {
                    continue;
                }
                KKBAutowired autowired = field.getAnnotation(KKBAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(KKBController.class)) {
                    Object instance = clazz.newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(KKBService.class)) {
                    KKBService annotation = clazz.getAnnotation(KKBService.class);
                    String beanName = annotation.value();
                    if ("".equals(beanName.trim())) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    // 默认使用接口名当做beanName，属于投机取巧的方式
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("The \"" + i.getName() + "\" is exists!");
                        }
                        ioc.put(i.getName(), instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        assert url != null;
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
                classNames.add(clazzName);
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

        if (!handlerMapping.containsKey(url)) {
            response.getWriter().write("404 Not Found!");
            return;
        }

        Method method = this.handlerMapping.get(url);
        Map<String, String[]> parameterMap = request.getParameterMap();

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class) {
                paramValues[i] = request;
            } else if (parameterType == HttpServletResponse.class) {
                paramValues[i] = response;
            } else if (parameterType == String.class) {
                Annotation[][] pa = method.getParameterAnnotations();
                for (Annotation[] annotations : pa) {
                    for (Annotation a : annotations) {
                        if (a instanceof KKBRequestParam) {
                            String paramName = ((KKBRequestParam) a).value();
                            if (!"".equals(paramName.trim())) {
                                String value = Arrays.toString(parameterMap.get(paramName)).replaceAll("[\\[\\]]",
                                        "").replaceAll("\\s", ",");
                                paramValues[i] = value;
                            }
                        }
                    }
                }
            }
        }

        method.invoke(ioc.get(toLowerFirstCase(method.getDeclaringClass().getSimpleName())), paramValues);
    }

}
