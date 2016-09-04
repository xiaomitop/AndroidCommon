package com.yt.commdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.yt.commdemo.bean.MainMenu;

import java.util.List;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class MainPageAdapter extends FragmentPagerAdapter{
    List<MainMenu> mainMenus;
    public MainPageAdapter(FragmentManager fm, List<MainMenu> mainMenus) {
        super(fm);
        this.mainMenus = mainMenus;
    }

    @Override
    public Fragment getItem(int position) {
        return mainMenus.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mainMenus.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
