package com.android.common.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.android.common.http.cookie.CookieJarImpl;
import com.android.common.http.cookie.store.MemoryCookieStore;
import com.android.common.http.https.HttpsUtils;
import com.android.common.http.request.FileRequest;
import com.android.common.http.request.Request;
import com.android.common.http.utlis.ByteArrayPool;
import com.android.common.http.utlis.PoolingByteArrayOutputStream;
import com.android.common.log.Logger;
import com.android.common.utils.ACFileUtil;
import com.android.common.utils.ACIOUtil;
import com.android.common.utils.ACSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp的执行器
 */
public class OkHttpStack implements I_HttpStack {
    private static OkHttpStack instance;
    private OkHttpClient mOkHttpClient;
    private Context context;

    public OkHttpStack(Context context) {
        this.context = context;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(new File(ACFileUtil.SD_CARD_PATH + "ac/" + "okhttpcache"), cacheSize);
        okHttpClientBuilder.addInterceptor(interceptor);
        okHttpClientBuilder.cache(cache);
        //cookie enabled
        okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient = okHttpClientBuilder.build();
        setCertificates();
        instance = this;
    }

    public static void createOkHttpStack(Context context) {
        if (instance == null) {
            synchronized (OkHttpStack.class) {
                if (instance == null) {
                    instance = new OkHttpStack(context);
                }
            }
        }
    }

    public static OkHttpStack instance() {
        if (instance == null) {
            synchronized (OkHttpStack.class) {
                if (instance == null) {
                    instance = new OkHttpStack(null);
                }
            }
        }
        return instance;
    }

    /**
     * 响应头拦截器，用来配置缓存策略
     */
    @SuppressWarnings("FieldCanBeLocal")
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request mRequest = (Request) chain.request().tag();
            okhttp3.Request.Builder okHttpRequestBuilder = chain.request().newBuilder();

            //如果context为空，没有手动创建createOkHttpStack实例，默认不去判断网络
            //无网络返回No address associated with hostname，而不是去读取缓存
            if (context != null) {
                //..如果网络未连接默认加载缓存
                if (!ACSystemUtils.isConnected(context)) {
                    okHttpRequestBuilder.cacheControl(CacheControl.FORCE_CACHE);
                    okHttpRequestBuilder.header("Cache-Control", "max-stale=" + 2419200);

                    okhttp3.Request request = okHttpRequestBuilder.build();
                    Response originalResponse = chain.proceed(request);

                    return originalResponse.newBuilder()
                            .header("Cache-Control", "max-stale=2419200")
                            .removeHeader("Pragma")
                            .build();
                } else {
                    //..是否由服务器控制缓存
                    if (!mRequest.isUseServerControl()) {
                        //是否允许缓存
                        if (mRequest.isShouldCache()) {
                            okHttpRequestBuilder.header("Cache-Control", "max-stale=" + mRequest.getCacheTime());
                            okhttp3.Request request = okHttpRequestBuilder.build();
                            Response originalResponse = chain.proceed(request);
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", "max-stale=" + mRequest.getCacheTime())
                                    .removeHeader("Pragma")
                                    .build();
                        } else {
                            okHttpRequestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
                            okhttp3.Request request = okHttpRequestBuilder.build();
                            Response originalResponse = chain.proceed(request);
                            return originalResponse.newBuilder().build();
                        }
                    } else {
                        String cacheControl = chain.request().cacheControl().toString();
                        if (TextUtils.isEmpty(cacheControl)) {
                            cacheControl = "public, max-age=0";
                        }
                        okhttp3.Request request = okHttpRequestBuilder.build();
                        Response originalResponse = chain.proceed(request);

                        return originalResponse.newBuilder()
                                .header("Cache-Control", cacheControl)
                                .removeHeader("Pragma")
                                .build();
                    }
                }
            } else {
                //..是否由服务器控制缓存
                if (!mRequest.isUseServerControl()) {
                    //是否允许缓存
                    if (mRequest.isShouldCache()) {
                        okHttpRequestBuilder.header("Cache-Control", "max-stale=" + mRequest.getCacheTime());
                        okhttp3.Request request = okHttpRequestBuilder.build();
                        Response originalResponse = chain.proceed(request);
                        return originalResponse.newBuilder()
                                .header("Cache-Control", "max-stale=" + mRequest.getCacheTime())
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        okHttpRequestBuilder.cacheControl(CacheControl.FORCE_NETWORK);
                        okhttp3.Request request = okHttpRequestBuilder.build();
                        Response originalResponse = chain.proceed(request);
                        return originalResponse.newBuilder().build();
                    }
                } else {
                    String cacheControl = chain.request().cacheControl().toString();
                    if (TextUtils.isEmpty(cacheControl)) {
                        cacheControl = "public, max-age=0";
                    }
                    okhttp3.Request request = okHttpRequestBuilder.build();
                    Response originalResponse = chain.proceed(request);

                    return originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        }
    };

    @Override
    public Call performRequest(final Request<?> request) throws IOException {
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        //请求超时时间
        int timeoutMs = request.getTimeoutMs();
        builder.connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMs + 5000, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs + 5000, TimeUnit.MILLISECONDS);

        OkHttpClient client = builder.build();
        okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();
        okHttpRequestBuilder.url(request.getUrl());
        okHttpRequestBuilder.tag(request);

        Map<String, String> headers = request.getHeaders();
        for (final String name : headers.keySet()) {
            okHttpRequestBuilder.addHeader(name, headers.get(name));
        }
        //构建请求
        setConnectionParametersForRequest(okHttpRequestBuilder, request);
        okhttp3.Request okHttpRequest = okHttpRequestBuilder.build();
        Call call = client.newCall(okHttpRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ExecutorDelivery.instance().postError(request, new OkhttpException(e));
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //测试缓存
                /*System.out.println("Response 1 response:" + response);
                System.out.println("Response 1 cache response:" + response.cacheResponse());
                System.out.println("Response 1 network response:" + response.networkResponse());*/
                ExecutorDelivery.instance().postStartHttp(request);
                byte[] responseContents = null;
                //header
                HashMap<String, String> responseHeaders = new HashMap<>();
                try {
                    int statusCode = response.code();
                    Headers okhttpResponseHeaders = response.headers();
                    for (int i = 0, len = okhttpResponseHeaders.size(); i < len; i++) {
                        final String name = okhttpResponseHeaders.name(i), value = okhttpResponseHeaders.value(i);
                        if (name != null) {
                            responseHeaders.put(name, value);
                        }
                    }

                    if (response.body().byteStream() != null) {
                        if (request instanceof FileRequest) {
                            responseContents = ((FileRequest) request).handleResponse(response);
                        } else {
                            responseContents = entityToBytes(response);
                        }
                    } else {
                        responseContents = new byte[0];
                    }

                    if (statusCode < 200 || statusCode > 299) {
                        throw new IOException();
                    }
                    NetworkResponse refactorResponse = new NetworkResponse(statusCode, responseContents, responseHeaders);
                    HttpResult<?> httpResult = request.parseNetworkResponse(refactorResponse);

                    //执行异步响应
                    /*if (networkResponse.data != null) {
                        if (request.getCallback() != null) {
                            request.getCallback().onSuccessInAsync(networkResponse.data);
                        }
                        mPoster.post(new Result(request.getUrl(),
                                networkResponse.headers, networkResponse.data));
                    }*/
                    ExecutorDelivery.instance().postResponse(request, httpResult);
                } catch (SocketTimeoutException e) {
                    parseAndDeliverNetworkError(request, new OkhttpException(
                            new SocketTimeoutException("socket timeout")));
                } catch (MalformedURLException e) {
                    parseAndDeliverNetworkError(request,
                            new OkhttpException("Bad URL " + request.getUrl(), e));
                } catch (IOException e) {
                    int statusCode;
                    NetworkResponse refactorResponse;
                    statusCode = response.code();
                    Logger.d(String.format("Unexpected response code %d for %s", statusCode,
                            e.toString()));

                    if (responseContents != null) {
                        refactorResponse = new NetworkResponse(statusCode, responseContents,
                                responseHeaders);
                        if (statusCode == HttpStatus.SC_UNAUTHORIZED
                                || statusCode == HttpStatus.SC_FORBIDDEN) {
                            parseAndDeliverNetworkError(request, new OkhttpException(refactorResponse,
                                    String.format("Unexpected response code %d for %s",
                                            statusCode, response.message())));
                        } else {
                            parseAndDeliverNetworkError(request, new OkhttpException(refactorResponse,
                                    String.format("Unexpected response code %d for %s",
                                            statusCode, response.message())));
                        }
                    } else {
                        parseAndDeliverNetworkError(request, new OkhttpException(String.format("Unexpected response code %d for %s",
                                statusCode, request.getUrl())));
                    }
                } catch (Exception e) {
                    Logger.d(String.format("Unhandled exception %s", e.getMessage()));
                    ExecutorDelivery.instance().postError(request, new OkhttpException(e));
                }
            }
        });

        return call;
    }

    private void parseAndDeliverNetworkError(Request<?> request, OkhttpException error) {
        error = request.parseNetworkError(error);
        ExecutorDelivery.instance().postError(request, error);
    }

    private static void setConnectionParametersForRequest(okhttp3.Request.Builder
                                                                  builder, Request<?> request)
            throws IOException {
        switch (request.getMethod()) {
            case Request.Method.GET:
                builder.get();
                break;
            case Request.Method.DELETE:
                builder.delete();
                break;
            case Request.Method.POST:
                builder.post(createRequestBody(request));
                break;
            case Request.Method.PUT:
                builder.put(createRequestBody(request));
                break;
            case Request.Method.HEAD:
                builder.head();
                break;
            case Request.Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;
            case Request.Method.TRACE:
                builder.method("TRACE", null);
                break;
            case Request.Method.PATCH:
                builder.patch(createRequestBody(request));
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static RequestBody createRequestBody(Request r) {
        byte[] body = r.getBody();
        if (body == null) {
            if (r.getMethod() == Request.Method.POST) {
                body = "".getBytes();
            } else {
                return null;
            }
        }
        return RequestBody.create(MediaType.parse(r.getBodyContentType()), body);
    }

    /**
     * 把HttpEntry转换为byte[]
     *
     * @throws IOException
     * @throws OkhttpException
     */
    private byte[] entityToBytes(Response httpResponse) throws IOException,
            OkhttpException {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
                ByteArrayPool.get(), (int) httpResponse.body().contentLength());
        byte[] buffer = null;
        try {
            InputStream in = httpResponse.body().byteStream();
            if (in == null) {
                throw new OkhttpException("server error");
            }
            buffer = ByteArrayPool.get().getBuf(1024);
            int count;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            ACIOUtil.closeIO(httpResponse.body().byteStream());
            ByteArrayPool.get().returnBuf(buffer);
            ACIOUtil.closeIO(bytes);
        }
    }

    /**
     * @param certificates HTTPS
     */
    public void setCertificates(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(certificates, null, null);
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        builder = builder.sslSocketFactory(sslSocketFactory);
        mOkHttpClient = builder.build();
    }
}