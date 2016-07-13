package com.android.common.mvp.presenter;

public interface IPresenter<T> {
    /**
     * 获取当前presenter泛型的类型
     * @return presenter的类型
     */
    Class<T> getViewClass();
}
