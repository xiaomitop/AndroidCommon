package com.yt.commdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.common.ui.avloadingindicatorview.AVLoadingIndicatorView;
import com.android.common.utils.ACStringUtils;
import com.yt.commdemo.R;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class EmptyLayout extends LinearLayout {

    public static final int ERROR = 1; // 错误
    public static final int LOADING = 2; // 加载中
    public static final int NODATA = 3; // 没有数据
    public static final int HIDE = 4; // 隐藏

    private int mErrorState = LOADING;
    private ImageView imgErrorState;
    private TextView textLoad;
    private AVLoadingIndicatorView barLoad;
    private String strTextLoadContent = "";
    private boolean clickEnable = false;
    private OnClickListener listener;

    public EmptyLayout(Context context) {
        super(context);
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.widget_empty, null);
        imgErrorState = (ImageView) view.findViewById(R.id.imgErrorState);
        textLoad = (TextView) view.findViewById(R.id.textLoad);
        barLoad = (AVLoadingIndicatorView) view.findViewById(R.id.barLoad);
        setBackgroundColor(-1);
        imgErrorState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        this.addView(view);
    }

    public void setEmptyOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void dismiss() {
        mErrorState = HIDE;
        setVisibility(View.GONE);
    }

    public void setTextLoadContent(String noDataContent) {
        strTextLoadContent = noDataContent;
    }

    public void setDataContent() {
        if (ACStringUtils.isEmpty(strTextLoadContent)) {
            textLoad.setText("糟糕，取不到数据勒，点击重试下吧~");
        } else {
            textLoad.setText(strTextLoadContent);
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case ERROR:
                mErrorState = ERROR;
                textLoad.setText("获取数据错误，点击重试下吧~");
                imgErrorState.setBackgroundResource(R.mipmap.page_failed_bg);
                imgErrorState.setVisibility(View.VISIBLE);
                barLoad.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case LOADING:
                mErrorState = LOADING;
                barLoad.setVisibility(View.VISIBLE);
                imgErrorState.setVisibility(View.GONE);
                textLoad.setText("加载中…");
                clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                imgErrorState.setBackgroundResource(R.mipmap.page_icon_empty);
                imgErrorState.setVisibility(View.VISIBLE);
                barLoad.setVisibility(View.GONE);
                setDataContent();
                clickEnable = true;
                break;
            case HIDE:
                dismiss();
                break;
            default:
                break;
        }
    }
}
