package com.kaikeba.springmvc.mapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述：
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-24 11:29:00
 */
public interface HandlerMapping {

    Object getHandler(HttpServletRequest request);

}
