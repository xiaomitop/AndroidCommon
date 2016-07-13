package com.android.common.http;

import java.util.Map;

/**
 * 功能：HTTP返回状态
 * 作者：yangtao
 * 创建时间：2016/4/29 15:32
 */
public class HttpResult<T> {
    /**
     * Http响应的类型
     */
    public final T result;

    public final OkhttpException error;

    public final Map<String, String> headers;

    public boolean isSuccess() {
        return error == null;
    }

    private HttpResult(T result, Map<String, String> headers) {
        this.result = result;
        this.error = null;
        this.headers = headers;
    }

    private HttpResult(OkhttpException error) {
        this.result = null;
        this.headers = null;
        this.error = error;
    }

    /**
     * 返回一个成功的HttpRespond
     *
     * @param result     Http响应的类型
     */
    public static <T> HttpResult<T> success(T result, Map<String, String> headers) {
        return new HttpResult<T>(result, headers);
    }

    /**
     * 返回一个失败的HttpRespond
     *
     * @param error 失败原因
     */
    public static <T> HttpResult<T> error(OkhttpException error) {
        return new HttpResult<T>(error);
    }
}
