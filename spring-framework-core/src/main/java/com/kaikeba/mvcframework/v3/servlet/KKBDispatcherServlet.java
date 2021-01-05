package com.kaikeba.mvcframework.v3.servlet;

import com.kaikeba.mvcframework.annotation.*;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private List<Handler> handlerMapping = new ArrayList<>();

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
                KKBRequestMapping requestMapping = method.getAnnotation(KKBRequestMapping.class);
                String regex = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new Handler(entry.getValue(), method, pattern));
                System.out.println("mapping :" + regex + "," + method);
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
        Handler handler = getHandler(request);

        if (handler == null) {
            response.getWriter().write("404 Not Found!");
            return;
        }

        Class<?>[] parameterTypes = handler.method.getParameterTypes();
        Object[] paramValues = new Object[parameterTypes.length];

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replaceAll("[\\[\\]]", "").replaceAll("\\s", ",");
            if (!handler.paramIndexMapping.containsKey(entry.getKey())) {
                continue;
            }
            int index = handler.paramIndexMapping.get(entry.getKey());
            paramValues[index] = convert(parameterTypes[index], value);
        }

        if (handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }

        if (handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }

        Object returnValue = handler.method.invoke(handler.controller, paramValues);
        if (returnValue == null) {
            return;
        }
        response.getWriter().write(returnValue.toString());
    }

    private Object convert(Class<?> parameterType, String value) {
        if (parameterType == Integer.class) {
            return Integer.valueOf(value);
        }
        // 其他类型
        return value;
    }

    private Handler getHandler(HttpServletRequest request) throws Exception {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        // 处理请求入参
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (Handler handler : handlerMapping) {
            try {
                Matcher matcher = handler.pattern.matcher(url);
                if (!matcher.matches()) {
                    continue;
                }
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    private class Handler {
        /**
         * 保存方法对应的实例
         */
        protected Object controller;

        /**
         * 保存映射的方法
         */
        protected Method method;

        protected Pattern pattern;

        /**
         * 参数顺序
         */
        protected Map<String, Integer> paramIndexMapping;

        public Handler(Object controller, Method method, Pattern pattern) {
            this.controller = controller;
            this.method = method;
            this.pattern = pattern;
            paramIndexMapping = new HashMap<>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {
            // 提取方法中加了注解的参数
            Annotation[][] pa = method.getParameterAnnotations();
            for (int i = 0; i < pa.length; i++) {
                for (Annotation a : pa[i]) {
                    if (a instanceof KKBRequestParam) {
                        String paramName = ((KKBRequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, i);
                        }
                    }
                }
            }
            // 提取request、response参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }
        }
    }

}
