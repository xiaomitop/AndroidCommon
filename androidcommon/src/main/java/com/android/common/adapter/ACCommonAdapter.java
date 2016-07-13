package com.android.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.common.adapter.interfaces.I_Adapter;
import java.util.List;

import static com.android.common.adapter.BaseAdapterHelper.get;


/**
 * ACCommonAdapter
 * 通用Adapter,适用于ListView/GridView,简化大量重复代码
 */
public abstract class ACCommonAdapter<T> extends BaseAdapter implements I_Adapter<T> {
    protected final Context context;
    protected final int layoutResId;
    protected final List<T> data;

    public abstract int getItemLayoutId();

    public ACCommonAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
        this.layoutResId = getItemLayoutId();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return position >= data.size() ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getLayoutResId(T item) {
        return this.layoutResId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final T item = getItem(position);
        final BaseAdapterHelper helper =
                get(context, convertView, parent, getLayoutResId(item), position);
        bindData(helper, position, item);
        helper.setAssociatedObject(item);
        return helper.getView();
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }
}
