package com.android.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.common.adapter.interfaces.I_Adapter;

import java.util.List;

import static com.android.common.adapter.BaseAdapterHelper.get;

/**
 * ACCommonAdapter
 * 通用Adapter,适用于RecyclerView,简化大量重复代码
 */
public abstract class ACCommonRecyclerAdapter<T> extends RecyclerView.Adapter implements I_Adapter<T> {
    protected final Context context;
    protected final int layoutResId;
    protected final List<T> data;

    public abstract int getItemLayoutId();

    public ACCommonRecyclerAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
        this.layoutResId = getItemLayoutId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseAdapterHelper helper = get(context, null, parent, viewType, -1);
        return new RecyclerViewHolder(helper.getView(), helper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseAdapterHelper helper = ((RecyclerViewHolder) holder).adapterHelper;
        helper.setAssociatedObject(getItem(position));
        bindData(helper, position, getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutResId(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return position >= data.size() ? null : data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getLayoutResId(T item) {
        return this.layoutResId;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        BaseAdapterHelper adapterHelper;

        public RecyclerViewHolder(View itemView, BaseAdapterHelper adapterHelper) {
            super(itemView);
            this.adapterHelper = adapterHelper;
        }

        @Override
        public void onClick(View v) {
            if (null != onItemClickListener) {
                onItemClickListener.onItemClick(RecyclerViewHolder.this, v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onItemLongClickListener) {
                onItemLongClickListener.onItemLongClick(RecyclerViewHolder.this, v, getAdapterPosition());
                return true;
            }
            return false;
        }
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }
}