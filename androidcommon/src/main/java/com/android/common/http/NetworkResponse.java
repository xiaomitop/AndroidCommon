package com.android.common.http;

import java.util.Collections;
import java.util.Map;

/**
 * 功能：重新构造Okhttp Response
 * 作者：yangtao
 * 创建时间：2016/4/29 11:42
 */
public class NetworkResponse {

    /**
     * @param statusCode 状态码
     * @param data 响应数据
     * @param headers 响应头
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap());
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers);
    }

    /**
     * HTTP 状态码
     */
    public final int statusCode;

    /**
     * 响应数据
     */
    public final byte[] data;

    /**
     * 响应头
     */
    public final Map<String, String> headers;
}
