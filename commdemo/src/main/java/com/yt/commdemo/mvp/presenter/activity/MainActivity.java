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
        mView.initFragment(savedInstanceState, fm);
    }

    @OnClick({R.id.btnHttp, R.id.btnRxjava, R.id.btnDB, R.id.btnWidget})
    public void tabMenuOnClick(View v) {
        int id = v.getId();
        int index = 0;
        if (id == R.id.btnHttp) {
            index = 0;
        } else if (id == R.id.btnRxjava) {
            index = 1;
        } else if (id == R.id.btnDB) {
            index = 2;
        } else if (id == R.id.btnWidget) {
            index = 3;
        }
        mView.restoreFragment(index);
    }
}
