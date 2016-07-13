package com.ac.sample.demo.mvp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.android.common.mvp.presenter.ActivityPresenterImpl;


public class MainActivity extends ActivityPresenterImpl<MainView> {

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        if(savedInstanceState == null) {
            mView.showFragment(fm);
        }else {
            mView.restoreFragment(fm);
        }
    }
}
