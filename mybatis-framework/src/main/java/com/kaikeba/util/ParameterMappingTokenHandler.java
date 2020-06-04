package com.kaikeba.util;

import com.kaikeba.framework.sqlsource.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：处理#{}占位符
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:38:00
 */
public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    @Override
    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        return new ParameterMapping(content);
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

}
