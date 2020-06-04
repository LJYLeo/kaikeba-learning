package com.kaikeba.util;

/**
 * 功能描述：占位符截取处理接口
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 11:49:34
 */
public interface TokenHandler {

    /**
     * 处理占位符
     *
     * @param content 占位符中的内容
     * @return
     */
    String handleToken(String content);

}
