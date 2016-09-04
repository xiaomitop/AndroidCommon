package com.yt.commdemo.mvp.presenter.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.android.common.mvp.presenter.ActivityPresenterImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.mvp.view.MainActivityView;

import butterknife.OnClick;

public class MainActivity extends ActivityPresenterImpl<MainActivityView> {

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        mView.initFragment(fm);
    }
}
