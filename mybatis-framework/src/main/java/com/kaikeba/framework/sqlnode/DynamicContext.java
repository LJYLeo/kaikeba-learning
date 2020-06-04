package com.kaikeba.framework.sqlnode;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：动态上下文，存储sqlNode执行过程中的内容
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:49:00
 */
public class DynamicContext {

    private StringBuffer sb = new StringBuffer();

    Map<String, Object> bindings = new HashMap<>();

    public DynamicContext(Object param) {
        this.bindings.put("_parameter", param);
    }

    public String getSql() {
        return sb.toString();
    }

    public void appendSql(String sqlText) {
        this.sb.append(sqlText);
        this.sb.append(" ");
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void setBindings(Map<String, Object> bindings) {
        this.bindings = bindings;
    }

}
