package com.ac.sample.application;

import com.android.common.application.ACApplication;
import com.android.common.http.OkHttpUtils;
import com.android.common.log.Logger;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/4/8 15:14
 */
public class MyApp extends ACApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        //手动创建OkHttp请求器，(如果不创建：无网络返回 No address associated with hostname错误，而不是去读取缓存)
        OkHttpUtils.createOkHttpStack(getContext());
    }

    @Override
    public void exceptionHandlerCallback(String infoPath) {
        super.exceptionHandlerCallback(infoPath);
        Logger.e("App", "LLLLLLLLLLLLLLLLLLLLL  " + infoPath);
    }
}
