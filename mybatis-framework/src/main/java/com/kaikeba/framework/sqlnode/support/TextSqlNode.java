package com.kaikeba.framework.sqlnode.support;

import com.kaikeba.framework.sqlnode.DynamicContext;
import com.kaikeba.framework.sqlnode.SqlNode;
import com.kaikeba.util.GenericTokenParser;
import com.kaikeba.util.OgnlUtils;
import com.kaikeba.util.SimpleTypeRegistry;
import com.kaikeba.util.TokenHandler;

/**
 * 功能描述：保存sql文本，包含${}
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 22:55:00
 */
public class TextSqlNode implements SqlNode {

    private String sqlText;

    public TextSqlNode(String sqlText) {
        this.sqlText = sqlText;
    }

    @Override
    public void apply(DynamicContext dynamicContext) {

        // 将${}替换成参数值
        String sql = new GenericTokenParser("${", "}", new BindingTokenHandler(dynamicContext)).parse(sqlText);
        dynamicContext.appendSql(sql);

    }

    public boolean isDynamic() {
        return sqlText.contains("${");
    }

    /**
     * ${}处理占位符方法
     */
    class BindingTokenHandler implements TokenHandler {

        private DynamicContext context;

        public BindingTokenHandler(DynamicContext context) {
            this.context = context;
        }

        @Override
        public String handleToken(String content) {

            // 取入参
            Object parameter = context.getBindings().get("_parameter");

            // 入参为空，直接返回空串
            if (parameter == null) {
                return "";
            } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
                // 如果是简单类型，直接返回该参数对应的字符串
                return String.valueOf(parameter);
            }

            // 如果是复杂类型，直接用ognl表达式取值
            Object value = OgnlUtils.getValue(content, parameter);

            return value == null ? "" : String.valueOf(value);

        }

    }

}
