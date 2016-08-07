package com.yt.commdemo.mvp.presenter.fragment;


import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.android.common.http.OkHttpUtils;
import com.android.common.http.callback.HttpCallback;
import com.android.common.http.callback.ProgressListener;
import com.android.common.log.Logger;
import com.android.common.mvp.presenter.FragmentPresenterImpl;
import com.android.common.utils.ACFileUtil;
import com.yt.commdemo.R;
import com.yt.commdemo.bean.News;
import com.yt.commdemo.bean.UpdateInfo;
import com.yt.commdemo.mvp.biz.NewsBiz;
import com.yt.commdemo.mvp.biz.UpdateInfoBiz;
import com.yt.commdemo.mvp.view.HttpFragmentView;
import com.yt.commdemo.widget.EmptyLayout;

import java.io.File;
import java.util.List;

import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class HttpFragment extends FragmentPresenterImpl<HttpFragmentView> {

    private NewsBiz newsBiz;
    private UpdateInfoBiz updateInfoBiz;
    private Call downloadCall;

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);
        newsBiz = new NewsBiz();
        updateInfoBiz = new UpdateInfoBiz();
    }

    @OnClick(R.id.btnGet)
    public void get(){
        mView.setEmptyLayoutState(EmptyLayout.LOADING);
        newsBiz.get(new HttpCallback(){
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                List<News> newsList = JSON.parseArray(t, News.class);
                mView.loadNews(newsList);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.setEmptyLayoutState(EmptyLayout.ERROR);
            }
        });
    }

    @OnClick(R.id.btnPost)
    public void post(){
        mView.setEmptyLayoutState(EmptyLayout.LOADING);
        newsBiz.post(new HttpCallback(){
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Logger.e("---->  ", "--------->  "+t);
                List<News> newsList = JSON.parseArray(t, News.class);
                mView.loadNews(newsList);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Logger.e("---->  ", "--------->  "+strMsg);
                mView.setEmptyLayoutState(EmptyLayout.ERROR);
            }
        });
    }

    @OnClick(R.id.btnJson)
    public void json(){
        mView.setEmptyLayoutState(EmptyLayout.LOADING);
        updateInfoBiz.json(new HttpCallback<UpdateInfo>(UpdateInfo.class){
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Logger.e("json", "json error: "+strMsg);
                mView.setEmptyLayoutState(EmptyLayout.ERROR);
            }

            @Override
            public void onSuccess(UpdateInfo updateInfo) {
                super.onSuccess(updateInfo);
                Logger.e("json", "json: "+updateInfo.toString());
                mView.loadJson(updateInfo.toString());
            }
        });
    }

    @OnClick(R.id.btnDownload)
    public void download(){
        ACFileUtil.deleteFolder(ACFileUtil.SD_CARD_PATH + "ac/download/");
        mView.download();
    }

    @OnClick(R.id.btnStart)
    public void downloadStart(){
        downloadCall = OkHttpUtils.download(ACFileUtil.SD_CARD_PATH + "ac/download/" + "a.apk",
                "http://p.gdown.baidu.com/b46c4690e712b33831fa53d1c7056845ccabc55c85785d47ac53aef86d3bec814602fbcbd7fbbfe5c6cfda60b4441f06d729a40ba6a7bcbd190f3c86bda9911ee2752d5e164babfd0e1d7b863a0e6491952b76a496d680fea201d50efe638eb53870d63377f240811dd1a32a4ba44783b7a82c489c316e795aa95b2c19f77ca43d385d6b2fec6f31195edea33b183dcc5daa895cb9f2a664c68841ca8d8386572f536c1bf0c2306a4dd4e14dc8d7931010ec89c8bafaac0d66de3005442c514499ad6707062c7753491e7317b6168b13f863b76b28644f82f307838e1e34a1d3014b35dba80ca60b300d3b5b648e9226973060b106211f6c",
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        mView.downloadProgress((int) totalSize, (int) transferredBytes);
                    }
                }, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.e("onSuccess", "====success" + t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Logger.e("onFailure", "download error: errorNo: "+errorNo + "   Msg: " + strMsg);
                    }
                });
    }

    @OnClick(R.id.btnStop)
    public void downloadStop(){
        if (downloadCall != null) {
            downloadCall.cancel();
            downloadCall = null;
        }
    }
}
