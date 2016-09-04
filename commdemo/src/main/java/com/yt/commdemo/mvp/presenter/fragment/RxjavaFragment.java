package com.yt.commdemo.mvp.presenter.fragment;

import android.view.View;

import com.android.common.mvp.presenter.FragmentPresenterImpl;
import com.yt.commdemo.mvp.view.RxjavaFragmentView;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class RxjavaFragment extends FragmentPresenterImpl<RxjavaFragmentView>{
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View view = this.getView();
        if (view != null) {
            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }
}
