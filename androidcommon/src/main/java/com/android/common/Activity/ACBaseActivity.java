package com.android.common.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * 功能：activity基类
 * 作者：yangtao
 * 创建时间：2016/4/8 13:41
 */
public abstract class ACBaseActivity extends AppCompatActivity implements I_Activity {

    public abstract int getContentViewId();
    protected View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACActivityStack.instance().addActivity(this);
        mRootView = getLayoutInflater().inflate(getContentViewId(), null, false);
        setContentView(mRootView);
        viewCreated(mRootView);
        ButterKnife.bind(this);
        initAllMembersView(savedInstanceState);
        initData();
        registerMethod();
    }

    @Override
    public void initData() {

    }

    @Override
    public void viewCreated(View rootView) {

    }

    @Override
    public void registerMethod() {

    }

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {

    }

    @Override
    public void unRegisterMethod() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterMethod();
        ACActivityStack.instance().finishActivity(this);
        ButterKnife.unbind(this);//解除绑定
    }
}
