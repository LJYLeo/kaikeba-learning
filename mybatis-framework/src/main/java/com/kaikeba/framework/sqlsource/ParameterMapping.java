package com.kaikeba.framework.sqlsource;

/**
 * 功能描述：参数类型映射
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 11:58:20
 */
public class ParameterMapping {

    private String name;

    private Class<?> type;

    public ParameterMapping(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

}
