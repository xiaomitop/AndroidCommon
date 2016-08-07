package com.yt.commdemo.mvp.view;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.common.mvp.view.ViewImpl;
import com.android.common.ui.recyclerview.DividerLine;
import com.yt.commdemo.R;
import com.yt.commdemo.adapter.NewsAdapter;
import com.yt.commdemo.adapter.UserAdapter;
import com.yt.commdemo.bean.User;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class DbFragmentView extends ViewImpl {
    private static final String TAG = "DbFragmentView";
    private RecyclerView rViewUser;
    private UserAdapter userAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_db;
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        rViewUser = ButterKnife.findById(rootView, R.id.rViewUser);
        rViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewUser.setItemAnimator(new DefaultItemAnimator());
        DividerLine dividerLine = new DividerLine();
        dividerLine.setColor(Color.BLACK);
        rViewUser.addItemDecoration(dividerLine);

    }

    public void notifyDataSetChanged(List<User> user, List<User> selectedList) {
        if (userAdapter == null) {
            userAdapter = new UserAdapter(getContext(), user, selectedList);
            rViewUser.setAdapter(userAdapter);
        } else {
            userAdapter.replaceAll(user);
        }
    }

    public void deleteOne(){
        userAdapter.deleteOne();
    }
}
