package com.yt.commdemo.mvp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.common.mvp.view.ViewImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.mvp.presenter.fragment.DbFragment;
import com.yt.commdemo.mvp.presenter.fragment.HttpFragment;
import com.yt.commdemo.mvp.presenter.fragment.RxjavaFragment;
import com.yt.commdemo.mvp.presenter.fragment.WidgetFragment;

import butterknife.ButterKnife;

public class MainActivityView extends ViewImpl {

    private FragmentManager fm;
    private RelativeLayout lloyoutContainer;
    private Fragment[] fragments;
    // 当前fragment的index
    private int currentTabIndex;
    private Button[] mTabs;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        lloyoutContainer = ButterKnife.findById(rootView, R.id.lLoyoutContainer);
        mTabs = new Button[4];
        mTabs[0] = ButterKnife.findById(rootView, R.id.btnHttp);
        mTabs[1] = ButterKnife.findById(rootView, R.id.btnRxjava);
        mTabs[2] = ButterKnife.findById(rootView, R.id.btnDB);
        mTabs[3] = ButterKnife.findById(rootView, R.id.btnWidget);
        // 把HTTPtab设为选中状态
        mTabs[0].setSelected(true);
    }

    @SuppressLint("CommitTransaction")
    public void initFragment(Bundle savedInstanceState, FragmentManager fm){
        this.fm = fm;
        HttpFragment httpFragment = (HttpFragment) fm.findFragmentByTag(HttpFragment.class.getName());
        if (httpFragment == null) {
            httpFragment = new HttpFragment();
        }
        RxjavaFragment rxjavaFragment = (RxjavaFragment) fm.findFragmentByTag(RxjavaFragment.class.getName());
        if (rxjavaFragment == null) {
            rxjavaFragment = new RxjavaFragment();
        }
        DbFragment dbFragment = (DbFragment) fm.findFragmentByTag(DbFragment.class.getName());
        if (dbFragment == null) {
            dbFragment = new DbFragment();
        }
        WidgetFragment uiFragment = (WidgetFragment) fm.findFragmentByTag(WidgetFragment.class.getName());
        if (uiFragment == null) {
            uiFragment = new WidgetFragment();
        }

        fragments = new Fragment[]{httpFragment, rxjavaFragment, dbFragment, uiFragment};
        if (savedInstanceState == null) {
            if (lloyoutContainer != null) {
                fm.beginTransaction().add(R.id.lLoyoutContainer, httpFragment, HttpFragment.class.getName())
                        .commit();
            }
        } else {
            FragmentTransaction trx = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (!(fragment instanceof HttpFragment)) {
                    if (!fragment.isAdded()) {
                        trx.add(R.id.lLoyoutContainer, fragment);
                    }
                    trx.hide(fragment);
                }
            }
            trx.commit();
        }
    }

    public void restoreFragment(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = fm.beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.lLoyoutContainer, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
}
