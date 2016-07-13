package com.android.common.http.request;

import com.android.common.http.HttpResult;
import com.android.common.http.OkhttpException;
import com.android.common.http.NetworkResponse;
import com.android.common.http.callback.HttpCallback;
import com.android.common.http.callback.ProgressListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个请求基类
 * @param <T> Http返回类型
 */
public abstract class Request<T> {

    private final RequestConfig mConfig;

    protected final HttpCallback mCallback;
    protected ProgressListener mProgressListener;

    public Request(RequestConfig config, HttpCallback callback) {
        if (config == null) {
            config = new RequestConfig();
        }
        mConfig = config;
        mCallback = callback;
    }

    /**
     * 支持的请求方式
     */
    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    /**
     * @param listener 进度监听
     */
    public void setOnProgressListener(ProgressListener listener) {
        mProgressListener = listener;
    }

    /**
     * @return 当前请求的方法
     */
    public int getMethod() {
        return mConfig.mMethod;
    }

    /**
     * @return 请求配置
     */
    public RequestConfig getConfig() {
        return mConfig;
    }

    public HttpCallback getCallback() {
        return mCallback;
    }

    public String getUrl() {
        return mConfig.mUrl;
    }

    public Map<String, String> getParams() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>();
    }

    protected String getParamsEncoding() {
        return mConfig.mEncoding;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    /**
     * 返回Http请求的body
     */
    public byte[] getBody() {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    /**
     * 对中文参数做URL转码
     */
    private byte[] encodeParameters(Map<String, String> params,
                                    String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (String entry : params.keySet()) {
                encodedParams.append(URLEncoder.encode(entry, paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(params.get(entry), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: "
                    + paramsEncoding, uee);
        }
    }

    /**
     * @return 请求超时时间
     */
    public final int getTimeoutMs() {
        return mConfig.mTimeout;
    }

    /**
     * 将网络请求执行器(NetWork)返回的NetWork响应转换为Http响应
     *
     * @param response 网络请求执行器(NetWork)返回的NetWork响应
     * @return 转换后的RefactorResponse, or null in the case of an error
     */
    abstract public HttpResult<T> parseNetworkResponse(NetworkResponse response);

    /**
     * 如果需要根据不同错误做不同的处理策略，可以在子类重写本方法
     */
    public OkhttpException parseNetworkError(OkhttpException e) {
        return e;
    }

    /**
     * 将Http请求结果分发到主线程
     *
     * @param response {@link #parseNetworkResponse(NetworkResponse)}
     */
    public abstract void deliverResponse(Map<String, String> headers, T response);

    /**
     * 响应Http请求异常的回调
     *
     * @param error 原因
     */
    public void deliverError(OkhttpException error) {
        if (mCallback != null) {
            int errorNo;
            String strMsg;
            if (error != null) {
                if (error.refactorResponse != null) {
                    errorNo = error.refactorResponse.statusCode;
                } else {
                    errorNo = -1;
                }
                strMsg = error.getMessage();
            } else {
                errorNo = -1;
                strMsg = "unknow";
            }
            mCallback.onFailure(errorNo, strMsg);
        }
    }

    public void deliverStartHttp() {
        if (mCallback != null) {
            mCallback.onPreHttp();
        }
    }

    /**
     * Http请求完成(不论成功失败)
     */
    public void requestFinish() {
        if (mCallback != null) {
            mCallback.onFinish();
        }
    }

    public int getCacheTime() {
        return mConfig.mCacheTime;
    }

    public boolean isShouldCache() {
        return mConfig.mShouldCache;
    }

    public boolean isUseServerControl() {
        return mConfig.mUseServerControl;
    }
}
