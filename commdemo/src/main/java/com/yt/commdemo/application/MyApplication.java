package com.yt.commdemo.application;

import com.android.common.application.ACApplication;
import com.android.common.http.OkHttpUtils;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class MyApplication extends ACApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //手动创建OkHttp请求器，(如果不创建：无网络返回 No address associated with hostname错误，而不是去读取缓存)
        OkHttpUtils.createOkHttpStack(getContext());
    }

    @Override
    public void exceptionHandlerCallback(String infoPath) {
        super.exceptionHandlerCallback(infoPath);
    }
}
