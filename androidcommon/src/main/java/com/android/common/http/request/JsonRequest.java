package com.android.common.http.request;


import com.android.common.http.HttpResult;
import com.android.common.http.NetworkResponse;
import com.android.common.http.callback.HttpCallback;
import com.android.common.log.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 用来发起application/json格式的请求的，我们平时所使用的是form表单提交的参数，而使用JsonRequest提交的是json参数。
 */
public class JsonRequest extends Request<byte[]> {

    private final String mRequestBody;
    private final HttpParams mParams;

    public JsonRequest(RequestConfig config, HttpParams params, HttpCallback callback) {
        super(config, callback);
        mRequestBody = params.getJsonParams();
        mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mParams.getHeaders();
    }

    @Override
    public void deliverResponse(Map<String, String> headers, byte[] response) {
        if (mCallback != null) {
            mCallback.onSuccess(headers, response);
        }
    }

    @Override
    public HttpResult<byte[]> parseNetworkResponse(NetworkResponse response) {
        return HttpResult.success(response.data, response.headers);
    }

    @Override
    public String getBodyContentType() {
        return String.format("application/json; charset=%s", getConfig().mEncoding);
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(getConfig().mEncoding);
        } catch (UnsupportedEncodingException uee) {
            Logger.e("getBody", String.format("Unsupported Encoding while trying to get the bytes of %s" +
                    " using %s", mRequestBody, getConfig().mEncoding));
            return null;
        }
    }
}
