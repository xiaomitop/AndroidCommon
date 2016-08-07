package com.android.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.android.common.adapter.interfaces.I_Adapter;
import com.android.common.adapter.interfaces.I_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public abstract class ACBaseRecyclerAdapter<T> extends RecyclerView.Adapter implements I_Data<T> {
    protected final Context context;
    protected final int layoutResId;
    protected final List<T> data;

    public abstract int getItemLayoutId();

    public ACBaseRecyclerAdapter(Context context, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = getItemLayoutId();
    }

    @Override
    public int getItemViewType(int position) {
        return this.layoutResId;
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
    public void add(T elem) {
        data.add(elem);
        notifyItemInserted(data.size());
    }

    @Override
    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyItemRangeInserted(data.size() - elem.size(), elem.size());
    }

    @Override
    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    @Override
    public void set(int index, T elem) {
        data.set(index, elem);
        notifyItemChanged(index);
    }

    @Override
    public void remove(T elem) {
        final int position = data.indexOf(elem);
        data.remove(elem);
        notifyItemRemoved(position);
    }

    @Override
    public void remove(int index) {
        data.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    @Override
    public boolean contains(T elem) {
        return data.contains(elem);
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
