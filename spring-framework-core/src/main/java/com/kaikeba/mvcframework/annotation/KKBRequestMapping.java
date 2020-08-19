package com.kaikeba.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：requestmapping注解
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:44:00
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KKBRequestMapping {

    /**
     * 值
     *
     * @return
     */
    String value() default "";

}
