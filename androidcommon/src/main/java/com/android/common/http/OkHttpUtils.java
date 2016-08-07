package com.android.common.http;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.common.http.callback.HttpCallback;
import com.android.common.http.callback.ProgressListener;
import com.android.common.http.request.FileRequest;
import com.android.common.http.request.FormRequest;
import com.android.common.http.request.HttpParams;
import com.android.common.http.request.JsonRequest;
import com.android.common.http.request.Request;
import com.android.common.http.request.RequestConfig;

import java.io.IOException;

import okhttp3.Call;

/**
 * 主入口
 */
public class OkHttpUtils {

    private OkHttpUtils() {
    }

    /**
     * 创建okhttp请求器
     */
    public static void createOkHttpStack(Context context) {
        OkHttpStack.createOkHttpStack(context);
    }

    /**
     * 请求方式:FORM表单,或 JSON内容传递
     */
    public interface ContentType {
        int FORM = 0;
        int JSON = 1;
    }

    /**
     * 构建器
     */
    public static class Builder {
        private HttpParams params;
        private int contentType;
        private HttpCallback callback;
        private Request<?> request;
        private ProgressListener progressListener;
        private RequestConfig httpConfig = new RequestConfig();

        /**
         * Http请求参数
         */
        public Builder params(HttpParams params) {
            this.params = params;
            return this;
        }

        /**
         * 参数的类型:FORM表单,或 JSON内容传递
         */
        public Builder contentType(int contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * 请求回调,不需要可以为空
         */
        public Builder callback(HttpCallback callback) {
            this.callback = callback;
            return this;
        }

        /**
         * HttpRequest
         */
        public Builder setRequest(Request<?> request) {
            this.request = request;
            return this;
        }

        /**
         * HttpRequest的配置器
         */
        public Builder httpConfig(RequestConfig httpConfig) {
            this.httpConfig = httpConfig;
            return this;
        }

        /**
         * 请求超时时间,如果不设置默认3000ms
         */
        public Builder timeout(int timeout) {
            this.httpConfig.mTimeout = timeout;
            return this;
        }

        /**
         * 上传进度回调
         *
         * @param listener 进度监听器
         */
        public Builder progressListener(ProgressListener listener) {
            this.progressListener = listener;
            return this;
        }

        /**
         * 缓存有效时间,单位分钟
         */
        public Builder cacheTime(int cacheTime) {
            this.httpConfig.mCacheTime = cacheTime;
            return this;
        }

        /**
         * 是否使用服务器控制的缓存有效期(如果使用服务器端的,则无视#cacheTime())
         */
        public Builder useServerControl(boolean useServerControl) {
            this.httpConfig.mUseServerControl = useServerControl;
            return this;
        }

        /**
         * 查看RequestConfig$Method
         * GET/POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
         */
        public Builder httpMethod(int httpMethod) {
            this.httpConfig.mMethod = httpMethod;
            if (httpMethod == Request.Method.POST) {
                this.httpConfig.mShouldCache = false;
            }
            return this;
        }

        /**
         * 是否启用缓存
         */
        public Builder shouldCache(boolean shouldCache) {
            this.httpConfig.mShouldCache = shouldCache;
            return this;
        }

        /**
         * 网络请求接口url
         */
        public Builder url(String url) {
            this.httpConfig.mUrl = url;
            return this;
        }

        /**
         * 编码,默认UTF-8
         */
        public Builder encoding(String encoding) {
            this.httpConfig.mEncoding = encoding;
            return this;
        }

        private Builder build() {
            if (request == null) {
                if (params == null) {
                    params = new HttpParams();
                } else {
                    if (httpConfig.mMethod == Request.Method.GET)
                        httpConfig.mUrl += params.getUrlParams();
                }

                if (contentType == ContentType.JSON) {
                    request = new JsonRequest(httpConfig, params, callback);
                } else {
                    request = new FormRequest(httpConfig, params, callback);
                }

                request.setOnProgressListener(progressListener);

                if (TextUtils.isEmpty(httpConfig.mUrl)) {
                    throw new RuntimeException("Request url is empty");
                }
            }
            if (callback != null) {
                callback.onPreStart();
            }
            return this;
        }

        /**
         * 执行请求任务
         */
        public Call doTask() {
            build();
            try {
                return OkHttpStack.instance().performRequest(request);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Call get(String url, HttpCallback callback) {
        return new Builder().url(url).callback(callback).doTask();
    }

    public static Call get(String url, HttpParams params, HttpCallback callback) {
        return new Builder().url(url).params(params).callback(callback).doTask();
    }

    public static Call get(String url, HttpParams params, boolean mShouldCache, HttpCallback callback) {
        return new Builder().url(url).params(params).callback(callback).shouldCache(mShouldCache).doTask();
    }

    public static Call post(String url, HttpParams params, HttpCallback callback) {
        return new Builder().url(url).params(params).httpMethod(Request.Method.POST).callback(callback).doTask();
    }

    public static Call post(String url, HttpParams params, ProgressListener listener,
                            HttpCallback callback) {
        return new Builder().url(url).params(params).progressListener(listener).httpMethod(Request.Method.POST)
                .callback(callback).doTask();
    }

    public static Call jsonGet(String url, HttpParams params, HttpCallback callback) {
        return new Builder().url(url).params(params).contentType(ContentType.JSON)
                .httpMethod(Request.Method.GET).callback(callback).doTask();
    }

    public static Call jsonPost(String url, HttpParams params, HttpCallback callback) {
        return new Builder().url(url).params(params).contentType(ContentType.JSON)
                .httpMethod(Request.Method.POST).callback(callback).doTask();
    }


    /**
     * 下载
     *
     * @param storeFilePath    本地存储绝对路径
     * @param url              要下载的文件的url
     * @param progressListener 下载进度回调
     * @param callback         回调
     */
    public static Call download(String storeFilePath, String url, ProgressListener
            progressListener, HttpCallback callback) {
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setOnProgressListener(progressListener);
        return new Builder().setRequest(request).doTask();
    }
}
