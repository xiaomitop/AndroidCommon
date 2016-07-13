package com.android.common.http.request;


import com.android.common.http.ExecutorDelivery;
import com.android.common.http.HttpResult;
import com.android.common.http.NetworkResponse;
import com.android.common.http.callback.HttpCallback;
import com.android.common.http.callback.ProgressListener;
import com.android.common.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Form表单形式的Http请求
 */
public class FormRequest extends Request<byte[]> {

    private final HttpParams mParams;

    public FormRequest(RequestConfig config, HttpParams params, HttpCallback callback) {
        super(config, callback);
        if (params == null) {
            params = new HttpParams();
        }
        this.mParams = params;
    }

    @Override
    public String getBodyContentType() {
        if (mParams.getContentType() != null) {
            return mParams.getContentType();
        } else {
            return super.getBodyContentType();
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return mParams.getHeaders();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            if (mProgressListener != null) {
                mParams.writeTo(new CountingOutputStream(bos, mParams.getContentLength(),
                        mProgressListener));
            } else {
                mParams.writeTo(bos);
            }
        } catch (IOException e) {
            Logger.e("Request", "FormRequest#getBody()--->IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public HttpResult<byte[]> parseNetworkResponse(NetworkResponse response) {
        return HttpResult.success(response.data, response.headers);
    }

    @Override
    public void deliverResponse(Map<String, String> headers, final byte[] response) {
        if (mCallback != null) {
            mCallback.onSuccess(headers, response);
        }
    }

    public static class CountingOutputStream extends FilterOutputStream {
        private final ProgressListener progListener;
        private long transferred;
        private long fileLength;

        public CountingOutputStream(final OutputStream out, long fileLength,
                                    final ProgressListener listener) {
            super(out);
            this.fileLength = fileLength;
            this.progListener = listener;
            this.transferred = 0;
        }

        public void write(int b) throws IOException {
            out.write(b);
            if (progListener != null) {
                this.transferred++;
                if ((transferred % 20 == 0) && (transferred <= fileLength)) {
                    ExecutorDelivery.instance().postProgress(this.progListener,
                            this.transferred, fileLength);
                }
            }
        }
    }
}
