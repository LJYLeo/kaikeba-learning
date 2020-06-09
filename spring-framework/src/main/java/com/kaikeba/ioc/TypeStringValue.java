package com.kaikeba.ioc;

import lombok.Data;

/**
 * 功能描述：简单类型的属性值
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-10 00:17:00
 */
@Data
public class TypeStringValue {

    private String name;

    private Class<?> targetType;

}
