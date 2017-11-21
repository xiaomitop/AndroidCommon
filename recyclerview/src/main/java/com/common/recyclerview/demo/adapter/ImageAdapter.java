package com.common.recyclerview.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.common.recyclerview.R;
import com.common.recyclerview.adapter.BaseAdapter;

import java.util.List;

public class ImageAdapter extends BaseAdapter<ImageAdapter.MyViewHolder> {

    //FinalBitmap fb = null;
    DisplayMetrics dm;

    public ImageAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
       // fb = FinalBitmap.create(context);
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_grid, parent, false);
        //动态设置ImageView的宽高，根据自己每行item数量计算
        //dm.widthPixels-dip2px(20)即屏幕宽度-左右10dp+10dp=20dp再转换为px的宽度，最后/3得到每个item的宽高
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((dm.widthPixels - dip2px(20)) / 3, (dm.widthPixels - dip2px(20)) / 3);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        String url = (String) listDatas.get(position);//转换
        //fb.display(holder.iv, url);
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv);
        }
    }

    int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
