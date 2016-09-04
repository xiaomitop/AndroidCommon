package com.yt.commdemo.mvp.presenter.fragment;

import android.view.View;

import com.android.common.log.Logger;
import com.android.common.mvp.presenter.FragmentPresenterImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.mvp.view.WidgetFragmentView;
import com.yt.commdemo.widget.SlideSwitch;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class WidgetFragment extends FragmentPresenterImpl<WidgetFragmentView> {

    @Bind(R.id.swit)
    SlideSwitch slide;

    @Override
    public void initData() {
        super.initData();
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.contact_option_array));
        mView.setDropdownListData(strings);

        slide.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                Logger.e("slide", "open");
            }

            @Override
            public void close() {
                Logger.e("slide", "close");
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        View view = this.getView();
        if (view != null) {
            view.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }
}
