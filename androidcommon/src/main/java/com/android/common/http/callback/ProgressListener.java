package com.android.common.http.callback;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/4/28 09:50
 */
public interface ProgressListener {
    void onProgress(long transferredBytes, long totalSize);
}