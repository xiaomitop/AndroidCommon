package com.yt.commdemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.common.utils.ACViewUtils;
import com.yt.commdemo.R;

import java.util.List;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/8/18 16:49
 */
public class DropdownList extends LinearLayout implements View.OnClickListener {
    private PopupWindow popupWindow = null;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    public DropdownList(Context context) {
        super(context);
        initView();
    }

    public DropdownList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DropdownList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View view = ACViewUtils.layoutToView(getContext(), R.layout.widget_dropdown_list);
        RelativeLayout rLayoutRoot = (RelativeLayout) view.findViewById(R.id.rLayoutRoot);
        rLayoutRoot.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView = new ListView(getContext());
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);
        this.addView(view);
    }

    @Override
    public void onClick(View view) {
        if (popupWindow == null) {
            showPopWindow();
        } else if (popupWindow.isShowing()) {
            closePopWindow();
        } else {
            showPopWindow();
        }
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow() {
        // 加载popupWindow的布局文件
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this.getWidth(), LayoutParams.WRAP_CONTENT);
            popupWindow.setContentView(listView);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.showAsDropDown(this);

    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
    }

    public void setData(String[] strings) {
        arrayAdapter.clear();
        arrayAdapter.addAll(strings);
    }

    public void setData(List<String> strings) {
        arrayAdapter.clear();
        arrayAdapter.addAll(strings);
    }
}
