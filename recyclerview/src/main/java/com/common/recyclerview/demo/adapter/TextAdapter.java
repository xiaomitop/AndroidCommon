package com.common.recyclerview.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.recyclerview.R;
import com.common.recyclerview.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by baiyuliang on 2016-5-27.
 */
public class TextAdapter extends BaseAdapter<TextAdapter.MyViewHolder> {

    public TextAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        String text = (String) listDatas.get(position);//转换
        holder.tv.setText(text);//填充数据
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}
