package com.android.common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 功能：Fragment基类
 * 作者：yangtao
 * 创建时间：2016/4/8 13:41
 */
public abstract class ACBaseFragment extends Fragment implements I_Fragment {
    public abstract int getContentViewId();
    protected Context context;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        viewCreated(mRootView);
        ButterKnife.bind(this, mRootView);//绑定framgent
        this.context = getActivity();
        initAllMembersView(savedInstanceState);
        initData();
        registerMethod();
        return mRootView;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {

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
    public void unRegisterMethod() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterMethod();
        ButterKnife.unbind(this);//解绑
    }
}
