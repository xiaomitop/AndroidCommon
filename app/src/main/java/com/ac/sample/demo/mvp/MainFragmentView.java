package com.ac.sample.demo.mvp;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ac.sample.R;
import com.android.common.mvp.view.ViewImpl;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainFragmentView extends ViewImpl {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        mListView = ButterKnife.findById(rootView, R.id.list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mvp_fragment;
    }

    public void newData(ArrayList<String> data) {
        mAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(mAdapter);
    }

    public void addData(ArrayList<String> data) {
        mAdapter.addAll(data);
    }

    public void toast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
