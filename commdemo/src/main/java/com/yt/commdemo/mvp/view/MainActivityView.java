package com.yt.commdemo.mvp.view;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.common.mvp.view.ViewImpl;
import com.android.common.utils.ACViewUtils;
import com.yt.commdemo.R;
import com.yt.commdemo.adapter.MainPageAdapter;
import com.yt.commdemo.bean.MainMenu;
import com.yt.commdemo.mvp.presenter.fragment.DbFragment;
import com.yt.commdemo.mvp.presenter.fragment.HttpFragment;
import com.yt.commdemo.mvp.presenter.fragment.RxjavaFragment;
import com.yt.commdemo.mvp.presenter.fragment.WidgetFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivityView extends ViewImpl {
    private RelativeLayout lloyoutContainer;
    private MainPageAdapter mainPageAdapter;
    private TabLayout tLayoutMainMenu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        lloyoutContainer = ButterKnife.findById(rootView, R.id.lLoyoutContainer);
        tLayoutMainMenu = ButterKnife.findById(rootView, R.id.tLayoutMainMenu);
    }
    public void initFragment(FragmentManager fm) {
        List<MainMenu> mainMenus = new ArrayList<>();
        mainMenus.add(new MainMenu(new HttpFragment(), R.mipmap.ic_bottom_menu_http, "HTTP"));
        mainMenus.add(new MainMenu(new RxjavaFragment(), R.mipmap.ic_bottom_menu_rx, "RXJAVA"));
        mainMenus.add(new MainMenu(new DbFragment(), R.mipmap.ic_bottom_menu_db, "DB"));
        mainMenus.add(new MainMenu(new WidgetFragment(), R.mipmap.ic_bottom_menu_ui, "UI"));
        mainPageAdapter = new MainPageAdapter(fm, mainMenus);
        for (int i = 0; i < mainMenus.size(); i++) {
            TextView textView = (TextView) ACViewUtils.layoutToView(getContext(), R.layout.button_main_menu);
            textView.setText(mainMenus.get(i).getText());
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getResources().getDrawable(mainMenus.get(i).getDrawableTop(), null);
            } else {
                drawable = getContext().getResources().getDrawable(mainMenus.get(i).getDrawableTop());
            }
            if (drawable != null) {
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
            }
            TabLayout.Tab tab = tLayoutMainMenu.newTab();
            tab.setCustomView(textView);
            if (i == 0) {
                tLayoutMainMenu.addTab(tab, true);
                Fragment fragment = (Fragment) mainPageAdapter.instantiateItem(lloyoutContainer, i);
                mainPageAdapter.setPrimaryItem(lloyoutContainer, i, fragment);
                mainPageAdapter.finishUpdate(lloyoutContainer);
            } else {
                tLayoutMainMenu.addTab(tab, false);
            }
        }
        tLayoutMainMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment fragment = (Fragment) mainPageAdapter.instantiateItem(lloyoutContainer, position);
                mainPageAdapter.setPrimaryItem(lloyoutContainer, position, fragment);
                mainPageAdapter.finishUpdate(lloyoutContainer);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
