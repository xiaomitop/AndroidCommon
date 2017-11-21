package com.common.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected Context context;//上下文
    protected List<Object> listDatas;//数据源
    protected LayoutInflater mInflater;
    protected OnViewClickListener onViewClickListener;//item子view点击事件
    private OnItemClickListener onItemClickListener;//item点击事件
    private OnItemLongClickListener onItemLongClickListener;//item长按事件

    public BaseAdapter(Context context, List<Object> listDatas) {
        init(context,listDatas);
    }

    /**
     * 如果item的子View有点击事件，可使用该构造方法
     * @param context
     * @param listDatas
     * @param onViewClickListener
     */
    public BaseAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        init(context,listDatas);
        this.onViewClickListener = onViewClickListener;
    }

    /**
     * 初始化
     * @param context
     * @param listDatas
     */
    private void init(Context context, List<Object> listDatas){
        this.context = context;
        this.listDatas = listDatas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {//item点击事件
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {//item长按事件
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    /**
     * item中子view的点击事件（回调）
     */
    public interface OnViewClickListener {
        /**
         * @param position item position
         * @param viewtype 点击的view的类型，调用时根据不同的view传入不同的值加以区分
         */
        void onViewClick(int position, int viewtype);
    }

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    /**
     * 设置item点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

}
