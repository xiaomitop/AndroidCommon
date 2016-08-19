package com.yt.commdemo.mvp.view;

import android.view.View;

import com.android.common.mvp.view.ViewImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.widget.DropdownList;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class WidgetFragmentView extends ViewImpl{
    private DropdownList dropdownList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_widget;
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        dropdownList = ButterKnife.findById(rootView, R.id.dropdownList);
    }

    public void setDropdownListData(List<String> strings){
        dropdownList.setData(strings);
    }
}
