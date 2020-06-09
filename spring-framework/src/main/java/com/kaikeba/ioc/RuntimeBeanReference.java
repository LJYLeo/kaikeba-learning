package com.kaikeba.ioc;

import lombok.Data;

/**
 * 功能描述：引用类型的属性
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-10 00:30:00
 */
@Data
public class RuntimeBeanReference {

    private String ref;

    public RuntimeBeanReference(String ref) {
        this.ref = ref;
    }

}
