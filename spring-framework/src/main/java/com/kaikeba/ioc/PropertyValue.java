package com.kaikeba.ioc;

import lombok.Data;

/**
 * 功能描述：property标签描述类
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-09 23:48:00
 */
@Data
public class PropertyValue {

    private String name;

    private Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

}
