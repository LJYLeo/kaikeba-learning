package com.kaikeba.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：autowired注解
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2020-08-13 09:41:00
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KKBAutowired {

    /**
     * 值
     *
     * @return
     */
    String value() default "";

}
