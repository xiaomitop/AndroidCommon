package com.android.common.exception;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/4/8 15:03
 */
public interface I_ExceptionHandlerCallback {
    /**
     * 崩溃日志回调接口，返回日志保存路径用于客户端自己处理
     *
     * @param infoPath 错误信息路径
     */
    void exceptionHandlerCallback(String infoPath);
}
