package com.android.common.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.android.common.http.callback.ProgressListener;
import com.android.common.http.request.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 分发HTTP响应
 */
public class ExecutorDelivery implements I_Delivery {

    public static ExecutorDelivery instance;
    /**
     * 用于分发HTTP响应到主线程
     */
    private final Executor mResponsePoster;

    public static ExecutorDelivery instance() {
        if (instance == null){
            instance = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
        }
        return instance;
    }

    /**
     * Creates a new response delivery interface.
     *
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                handler.post(command);
            }
        };
    }

    @Override
    public void postResponse(Request<?> request, HttpResult<?> httpResult) {
        postResponse(request, httpResult, null);
    }

    @Override
    public void postResponse(Request<?> request, HttpResult<?> httpResult, Runnable runnable) {
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, httpResult, runnable));
    }

    @Override
    public void postError(Request<?> request, OkhttpException error) {
        HttpResult<?> httpResult = HttpResult.error(error);
        mResponsePoster.execute(new ResponseDeliveryRunnable(request, httpResult, null));
    }

    @Override
    public void postStartHttp(final Request<?> request) {
        mResponsePoster.execute(new Runnable() {
            @Override
            public void run() {
                request.deliverStartHttp();
            }
        });
    }

    @Override
    public void postProgress(final ProgressListener listener, final long transferredBytes,
                             final long totalSize) {
        mResponsePoster.execute(new Runnable() {
            @Override
            public void run() {
                listener.onProgress(transferredBytes, totalSize);
            }
        });
    }

    /**
     * 分发到主线程
     */
    private static class ResponseDeliveryRunnable implements Runnable {
        private final Request mRequest;
        private final HttpResult mHttpResult;
        private final Runnable mRunnable;

        public ResponseDeliveryRunnable(Request request, HttpResult httpResult, Runnable runnable) {
            mRequest = request;
            mHttpResult = httpResult;
            mRunnable = runnable;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if (mHttpResult.isSuccess()) {
                Map<String, String> headers = new HashMap<>();
                if (mHttpResult.headers != null) {
                    Set<Map.Entry<String, String>> entrySet = mHttpResult.headers.entrySet();
                    for (Map.Entry<String, String> entry : entrySet) {
                        headers.put(entry.getKey(), entry.getValue());
                    }
                }
                mRequest.deliverResponse(headers, mHttpResult.result);
            } else {
                mRequest.deliverError(mHttpResult.error);
            }
            mRequest.requestFinish();
            if (mRunnable != null) {
                mRunnable.run();
            }
        }
    }
}
