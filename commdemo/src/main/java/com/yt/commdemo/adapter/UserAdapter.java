package com.yt.commdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.common.adapter.ACBaseRecyclerAdapter;
import com.android.common.log.Logger;
import com.yt.commdemo.R;
import com.yt.commdemo.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/7 0007.
 */
public class UserAdapter  extends ACBaseRecyclerAdapter<User> {
    protected final LayoutInflater mLayoutInflater;
    private HashMap<String, Boolean> checks; //用于保存checkBox的选择状态
    private List<User> selectedList;
    public UserAdapter(Context context, List<User> data, List<User> selectedList) {
        super(context, data);
        mLayoutInflater = LayoutInflater.from(context);
        checks = new HashMap<>();
        this.selectedList = selectedList;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_user_adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mLayoutInflater.inflate(layoutResId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            ((RecyclerViewHolder) holder).bindViewData(data.get(position), position);
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textId)
        TextView textId;
        @Bind(R.id.textName)
        TextView textName;
        @Bind(R.id.checkBox)
        CheckBox checkBox;

        RecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewData(final User user, final int position) {
            textId.setText(String.valueOf(user.getId()));
            textName.setText(user.getName());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checks.put(String.valueOf(position), isChecked);
                    if (isChecked) {
                        selectedList.add(user);
                    } else {
                        selectedList.remove(user);
                    }
                }
            });

            Boolean isChecked = checks.get(String.valueOf(position));
            checkBox.setChecked(isChecked != null ? isChecked : false);
        }
    }

    public void deleteOne(){
        checks.clear();
        selectedList.clear();
    }
}
