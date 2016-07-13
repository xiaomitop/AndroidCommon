package com.android.common.application;

import android.app.Application;
import android.content.Context;

import com.android.common.exception.ACCrashHandler;
import com.android.common.exception.I_ExceptionHandlerCallback;

/**
 * 功能：Application基类
 * 作者：yangtao
 * 创建时间：2016/4/8 14:16
 */
public abstract class ACApplication extends Application implements I_ExceptionHandlerCallback{

    private static ACApplication instance;

    public static ACApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //错误日志捕捉
        ACCrashHandler crashHandler = ACCrashHandler.instance();
        crashHandler.initCrashHandler(getApplicationContext(), this);
    }

    public Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void exceptionHandlerCallback(String infoPath) {
        // ..崩溃日志自定义处理
    }
}
