package com.kaikeba.framework.sqlsource;

/**
 * 功能描述：
 * 暗号：开课吧老詹最帅
 *
 * @author 刘嘉宇
 * @version 1.0.0
 * @date 2020-06-04 11:49:34
 */
public interface SqlSource {

    BoundSql getBoundSql(Object param);

}
