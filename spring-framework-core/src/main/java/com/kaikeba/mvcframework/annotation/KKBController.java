package com.kaikeba.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：controller注解
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:42:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KKBController {

    /**
     * 值
     *
     * @return
     */
    String value() default "";

}
