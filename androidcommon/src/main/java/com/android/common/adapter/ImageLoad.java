package com.android.common.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.common.adapter.interfaces.I_ImageLoad;
import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class ImageLoad implements I_ImageLoad{
    @Override
    public void load(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context).load(imageUrl).into(imageView);
    }
}
