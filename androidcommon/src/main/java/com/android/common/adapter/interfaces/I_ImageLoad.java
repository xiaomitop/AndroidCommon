package com.android.common.adapter.interfaces;

import android.content.Context;
import android.widget.ImageView;

/**
 * ACCommonAdapter
 * 网络图片加载接口规范
 */
public interface I_ImageLoad {
    void load(Context context, ImageView imageView, String imageUrl);
}
