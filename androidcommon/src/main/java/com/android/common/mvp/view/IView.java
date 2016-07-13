package com.android.common.mvp.view;

import android.content.Context;
import android.view.View;

import com.android.common.mvp.presenter.IPresenter;

public interface IView {

    /**
     * view创建完成
     */
    void viewCreated(View rootView);

    /**
     * 上下文
     */
    Context getContext();

    /**
     * 返回当前视图需要的layout的id
     * @return layout id
     */
    int getLayoutId();

    /**
     * 绑定Presenter
     * @param presenter presenter
     */
    void bindPresenter(IPresenter presenter);
}
