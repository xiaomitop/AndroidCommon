package com.android.common.http.request;

import android.text.TextUtils;

import com.android.common.http.ExecutorDelivery;
import com.android.common.http.HttpResult;
import com.android.common.http.OkhttpException;
import com.android.common.http.NetworkResponse;
import com.android.common.http.callback.HttpCallback;
import com.android.common.log.Logger;
import com.android.common.utils.ACIOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import okhttp3.Response;

/**
 * 请求文件方法类
 */
public class FileRequest extends Request<byte[]> {
    private final File mStoreFile;
    private final File mTemporaryFile; // 临时文件

    private Map<String, String> mHeaders = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileRequest(String storeFilePath, RequestConfig config, HttpCallback callback) {
        super(config, callback);
        mStoreFile = new File(storeFilePath);
        File folder = mStoreFile.getParentFile();

        if (folder != null && folder.mkdirs()) {
            if (!mStoreFile.exists()) {
                try {
                    mStoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mTemporaryFile = new File(storeFilePath + ".tmp");
    }

    public File getStoreFile() {
        return mStoreFile;
    }

    public File getTemporaryFile() {
        return mTemporaryFile;
    }

    @Override
    public HttpResult<byte[]> parseNetworkResponse(NetworkResponse response) {
        String errorMessage;
        if (mTemporaryFile.canRead() && mTemporaryFile.length() > 0) {
            if (mTemporaryFile.renameTo(mStoreFile)) {
                return HttpResult.success(response.data, response.headers);
            } else {
                errorMessage = "Can't rename the download temporary file!";
            }
        } else {
            errorMessage = "Download temporary file was invalid!";
        }

        return HttpResult.error(new OkhttpException(errorMessage));
    }

    @Override
    public Map<String, String> getHeaders() {
        mHeaders.put("Range", "bytes=" + mTemporaryFile.length() + "-");
        mHeaders.put("Accept-Encoding", "identity");
        return mHeaders;
    }

    public Map<String, String> putHeader(String k, String v) {
        mHeaders.put(k, v);
        return mHeaders;
    }

    public static boolean isSupportRange(Response response) {
        if (TextUtils.equals(getHeader(response, "Accept-Ranges"), "bytes")) {
            return true;
        }
        String value = getHeader(response, "Content-Range");
        return value != null && value.startsWith("bytes");
    }

    public static String getHeader(Response response, String key) {
        return response.header(key);
    }

    public static boolean isGzipContent(Response response) {
        return TextUtils.equals(getHeader(response, "Content-Encoding"), "gzip");
    }

    public byte[] handleResponse(Response response) throws IOException {
        long fileSize = response.body().contentLength();
        if (fileSize <= 0) {
            Logger.d("Response doesn't present Content-Length!");
        }

        long downloadedSize = mTemporaryFile.length();
        boolean isSupportRange = isSupportRange(response);
        if (isSupportRange) {
            fileSize += downloadedSize;

            String realRangeValue = response.header("Content-Range");
            if (!TextUtils.isEmpty(realRangeValue)) {
                String assumeRangeValue = "bytes " + downloadedSize + "-" + (fileSize - 1);
                if (TextUtils.indexOf(realRangeValue, assumeRangeValue) == -1) {
                    throw new IllegalStateException(
                            "The Content-Range Header is invalid Assume["
                                    + assumeRangeValue + "] vs Real["
                                    + realRangeValue + "], "
                                    + "please remove the temporary file ["
                                    + mTemporaryFile + "].");
                }
            }
        }

        if (fileSize > 0 && mStoreFile.length() == fileSize) {
            //noinspection ResultOfMethodCallIgnored
            mStoreFile.renameTo(mTemporaryFile);
            if (mProgressListener != null)
                ExecutorDelivery.instance().postProgress(mProgressListener,
                        fileSize, fileSize);
            return null;
        }

        RandomAccessFile tmpFileRaf = new RandomAccessFile(mTemporaryFile, "rw");
        if (isSupportRange) {
            tmpFileRaf.seek(downloadedSize);
        } else {
            tmpFileRaf.setLength(0);
            downloadedSize = 0;
        }

        InputStream in = response.body().byteStream();
        try {
            if (isGzipContent(response) && !(in instanceof GZIPInputStream)) {
                in = new GZIPInputStream(in);
            }
            byte[] buffer = new byte[6 * 1024]; // 6K buffer
            int offset;

            while ((offset = in.read(buffer)) != -1) {
                tmpFileRaf.write(buffer, 0, offset);
                downloadedSize += offset;

                //下载进度回调
                if (mProgressListener != null) {
                    ExecutorDelivery.instance().postProgress(mProgressListener,
                            downloadedSize, fileSize);
                }
            }
        } finally {
            ACIOUtil.closeIO(in, response.body().byteStream(), tmpFileRaf);
        }
        return null;
    }

    @Override
    public void deliverResponse(Map<String, String> headers, byte[] response) {
        if (mCallback != null) {
            if (response == null) response = new byte[0];
            mCallback.onSuccess(headers, response);
        }
    }
}
