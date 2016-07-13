package com.android.common.http.callback;

import com.alibaba.fastjson.JSON;
import com.android.common.log.Logger;

import java.util.Map;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/4/28 09:48
 */
public class HttpCallback<T> {

    private Class<T> mClazz;

    public HttpCallback() {

    }

    public HttpCallback(Class<T> clazz) {
        this.mClazz = clazz;
    }

    /**
     * 请求开始之前回调
     */
    public void onPreStart() {
    }

    /**
     * 发起Http之前调用(只要是内存缓存中没有就会被调用)
     */
    public void onPreHttp() {
    }

    /**
     * 注意：本方法将在异步调用。
     * Http异步请求成功时在异步回调,并且仅当本方法执行完成才会继续调用onSuccess()
     *
     * @param b 返回的信息
     */
    public void onSuccessInAsync(byte[] b) {
    }

    /**
     * Http请求成功时回调
     *
     * @param t HttpRequest返回信息
     */
    public void onSuccess(T t) {
    }

    /**
     * Http请求成功时回调
     *
     * @param t HttpRequest返回信息
     */
    public void onSuccess(String t) {

    }

    /**
     * Http请求成功时回调
     *      如果mClazz不为空，且不为String.class，默认json解析,这里json解析是在主线程做的，不能长时间耗时
     * @param headers HttpRespond头
     * @param b       HttpRequest返回信息
     */
    public void onSuccess(Map<String, String> headers, byte[] b) {
        String s = new String(b);
        if (mClazz == null || mClazz.equals(String.class)) {
            onSuccess(s);
        } else {
            onSuccess(s);
            try {
                onSuccess(JSON.parseObject(s, mClazz));
            }catch (Exception e){
                Logger.e("JSON ERROR", "JSON ERROR:"+e.toString());
            }
        }
    }

    /**
     * Http请求失败时回调
     *
     * @param errorNo 错误码
     * @param strMsg  错误原因
     */
    public void onFailure(int errorNo, String strMsg) {
    }

    /**
     * Http请求结束后回调
     */
    public void onFinish() {
    }
}
