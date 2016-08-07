package com.ac.sample.demo.mvp;

import com.ac.sample.R;
import com.android.common.mvp.presenter.FragmentPresenterImpl;

import java.util.ArrayList;

import butterknife.OnClick;

public class MyFragment extends FragmentPresenterImpl<MainFragmentView> {
    @OnClick(R.id.newdata)
    void newData() {
        new MainBiz().info(new MainBiz.Callback<ArrayList<String>>() {
            @Override
            public void callback(ArrayList<String> data) {
                if(data == null || data.isEmpty()) {
                    mView.toast("没有数据");
                    return;
                }
                mView.newData(data);
            }
        });
    }

    @OnClick(R.id.adddata)
    void addData() {
        new MainBiz().info(new MainBiz.Callback<ArrayList<String>>() {
            @Override
            public void callback(ArrayList<String> data) {
                if(data == null || data.isEmpty()) {
                    mView.toast("没有数据");
                    return;
                }
                mView.addData(data);
            }
        });
    }
}
