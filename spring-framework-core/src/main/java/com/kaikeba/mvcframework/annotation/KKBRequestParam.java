package com.kaikeba.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：requestparam注解
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:45:00
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KKBRequestParam {

    /**
     * 值
     *
     * @return
     */
    String value() default "";

}
