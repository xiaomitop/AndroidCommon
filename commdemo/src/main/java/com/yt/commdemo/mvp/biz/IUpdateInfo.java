package com.yt.commdemo.mvp.biz;

import com.android.common.http.callback.HttpCallback;
import com.yt.commdemo.bean.UpdateInfo;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public interface IUpdateInfo {
    void json(HttpCallback<UpdateInfo> httpCallback);
}
