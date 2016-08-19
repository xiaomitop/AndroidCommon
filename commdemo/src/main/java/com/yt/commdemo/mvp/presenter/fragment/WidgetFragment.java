package com.yt.commdemo.mvp.presenter.fragment;

import com.android.common.mvp.presenter.FragmentPresenterImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.mvp.view.WidgetFragmentView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class WidgetFragment extends FragmentPresenterImpl<WidgetFragmentView>{

    @Override
    public void initData() {
        super.initData();
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.contact_option_array));
        mView.setDropdownListData(strings);
    }
}
