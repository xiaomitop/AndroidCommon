package com.ac.sample.demo.mvp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ac.sample.R;
import com.android.common.mvp.view.ViewImpl;

public class MainView extends ViewImpl {

    public void showFragment(FragmentManager fm) {
        fm.beginTransaction().add(R.id.container, new MyFragment(), "my").commit();
    }

    public void restoreFragment(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag("my");
        if(fragment == null) {
            showFragment(fm);
        }else {
            fm.beginTransaction().show(fragment).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.mvp_activity_main;
    }
}
