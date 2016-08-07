package com.yt.commdemo.mvp.biz;

import com.android.common.http.callback.HttpCallback;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public interface INewsBiz {
    //这里直接用Http框架中的回调借口
    void get(HttpCallback httpCallback);
    void post(HttpCallback httpCallback);
}
