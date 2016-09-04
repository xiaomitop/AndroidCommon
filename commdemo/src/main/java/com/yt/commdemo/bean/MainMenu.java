package com.yt.commdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class MainMenu implements Parcelable {
    private Fragment fragment;
    private int drawableTop;
    private String text;

    public MainMenu(Fragment fragment, int drawableTop, String text) {
        this.fragment = fragment;
        this.drawableTop = drawableTop;
        this.text = text;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getDrawableTop() {
        return drawableTop;
    }

    public void setDrawableTop(int drawableTop) {
        this.drawableTop = drawableTop;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
