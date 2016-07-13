package com.android.common.http;


import com.android.common.http.callback.ProgressListener;
import com.android.common.http.request.Request;

/**
 * 分发器，将异步线程中的结果响应到UI线程中
 */
public interface I_Delivery {
    /**
     * 分发响应结果
     *
     * @param request
     * @param httpResult
     */
    void postResponse(Request<?> request, HttpResult<?> httpResult);

    /**
     * 分发Failure事件
     *
     * @param request 请求
     * @param error   异常原因
     */
    void postError(Request<?> request, OkhttpException error);

    void postResponse(Request<?> request, HttpResult<?> httpResult, Runnable runnable);

    /**
     * 分发当Http请求开始时的事件
     */
    void postStartHttp(Request<?> request);

    /**
     * 进度
     *
     * @param transferredBytes 进度
     * @param totalSize        总量
     */
    void postProgress(ProgressListener listener, long transferredBytes, long totalSize);
}
