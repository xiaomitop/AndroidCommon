package com.yt.commdemo.mvp.biz;

import com.android.common.http.OkHttpUtils;
import com.android.common.http.callback.HttpCallback;
import com.android.common.log.Logger;
import com.yt.commdemo.bean.UpdateInfo;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class UpdateInfoBiz implements IUpdateInfo{

    @Override
    public void json(HttpCallback<UpdateInfo> httpCallback) {
        OkHttpUtils.get("http://git.oschina.net/xiaomitop/UpdateApp/raw/master/update/json_update", httpCallback);
    }
}
