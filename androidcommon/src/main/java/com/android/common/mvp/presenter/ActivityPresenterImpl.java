package com.android.common.mvp.presenter;

import android.view.View;

import com.android.common.Activity.ACBaseActivity;
import com.android.common.mvp.helper.GenericHelper;
import com.android.common.mvp.view.IView;

public class ActivityPresenterImpl<T extends IView> extends ACBaseActivity implements IPresenter<T> {

    protected T mView;

    @Override
    public int getContentViewId() {
        try {
            mView = getViewClass().newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return mView.getLayoutId();
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        mView.viewCreated(rootView);
        mView.bindPresenter(this);
    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }
}
