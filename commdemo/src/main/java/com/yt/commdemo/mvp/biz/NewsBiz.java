package com.yt.commdemo.mvp.biz;

import com.android.common.http.OkHttpUtils;
import com.android.common.http.callback.HttpCallback;
import com.android.common.http.request.HttpParams;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class NewsBiz implements INewsBiz{

    @Override
    public void get(HttpCallback httpCallback) {
        OkHttpUtils.get("http://git.oschina.net/xiaomitop/UpdateApp/raw/master/pagehpme/json_news_list", httpCallback);
    }

    @Override
    public void post(HttpCallback httpCallback) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("dir", "0");
        OkHttpUtils.post("http://git.oschina.net/xiaomitop/UpdateApp/raw/master/pagehpme/json_news_list", httpParams, httpCallback);
    }
}
