package com.android.common.adapter.interfaces;


import com.android.common.adapter.BaseAdapterHelper;

/**
 * ACCommonAdapter
 * 扩展的Adapter接口规范
 */
public interface I_Adapter<T> {

    void bindData(BaseAdapterHelper helper, int position, T item);

    int getLayoutResId(T item);
}
