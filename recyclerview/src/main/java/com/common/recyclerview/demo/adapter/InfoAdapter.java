package com.common.recyclerview.demo.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.common.recyclerview.R;
import com.common.recyclerview.adapter.BaseAdapter;
import com.common.recyclerview.demo.bean.InfoBean;

import java.util.List;

public class InfoAdapter extends BaseAdapter<InfoAdapter.MyViewHolder> {

    public InfoAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_info, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        InfoBean infoBean = (InfoBean) listDatas.get(position);//转换
        holder.tv.setText(infoBean.getText());//填充数据

        if(infoBean.getImgList()!=null&&infoBean.getImgList().size()>0){
            ImageAdapter imageAdapter = new ImageAdapter(context, infoBean.getImgList());
            holder.rv_grid.setLayoutManager(new GridLayoutManager(context,3));
            holder.rv_grid.setAdapter(imageAdapter);
            holder.rv_grid.setVisibility(View.VISIBLE);
        }else{
            holder.rv_grid.setVisibility(View.GONE);
        }

        holder.iv_z.setOnClickListener(new ViewClikListener(onViewClickListener, position, 1));//赞 viewtype=1代表赞点击事件
        holder.iv_pl.setOnClickListener(new ViewClikListener(onViewClickListener, position, 2));//评论 viewtype=2代表评论点击事件
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;//内容
        ImageView iv_z, iv_pl;//赞，评论
        RecyclerView rv_grid;

        public MyViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.tv);
            rv_grid = view.findViewById(R.id.rv_grid);
        }
    }

    /**
     * view的点击事件
     */
    class ViewClikListener implements View.OnClickListener {

        OnViewClickListener onViewClickListener;
        int position;
        int viewtype;

        public ViewClikListener(OnViewClickListener onViewClickListener, int position, int viewtype) {
            this.onViewClickListener = onViewClickListener;
            this.position = position;
            this.viewtype = viewtype;
        }

        @Override
        public void onClick(View v) {
            onViewClickListener.onViewClick(position, viewtype);
        }
    }

}
