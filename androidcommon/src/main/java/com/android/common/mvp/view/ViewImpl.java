package com.android.common.mvp.view;

import android.content.Context;
import android.view.View;

import com.android.common.mvp.presenter.IPresenter;


/**
 * View层的基类
 */
public abstract class ViewImpl implements IView {

    private Context context;

    /**
     * 绑定的presenter
     */
    protected IPresenter mPresenter;

    @Override
    public void viewCreated(View rootView) {
        context = rootView.getContext();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void bindPresenter(IPresenter presenter) {
        mPresenter = presenter;
    }
}
