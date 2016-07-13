package com.android.common.http;

import com.android.common.http.request.Request;

import java.io.IOException;

import okhttp3.Call;

/**
 * 功能：Http请求端定义
 * 作者：yangtao
 * 创建时间：2016/4/28 09:25
 */
public interface I_HttpStack {
    /**
     * 让Http请求端去发起一个Request
     * @param request           一次实际请求集合
     */
    Call performRequest(Request<?> request)
            throws IOException;
}
