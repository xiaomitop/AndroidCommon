package com.yt.commdemo.mvp.view;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.common.mvp.view.ViewImpl;
import com.android.common.ui.recyclerview.DividerLine;
import com.yt.commdemo.R;
import com.yt.commdemo.adapter.NewsAdapter;
import com.yt.commdemo.bean.News;
import com.yt.commdemo.widget.EmptyLayout;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class HttpFragmentView extends ViewImpl {
    private RecyclerView listviewNews;
    private NewsAdapter newsAdapter;
    private TextView textJson;
    private LinearLayout lLayoutDownload;
    private ProgressBar barDownload;
    private EmptyLayout emptyLayout;

    @Override
    public int getLayoutId() {
        return R.layout.frament_http;
    }

    @Override
    public void viewCreated(View rootView) {
        super.viewCreated(rootView);
        emptyLayout = ButterKnife.findById(rootView, R.id.emptyLayout);
        listviewNews = ButterKnife.findById(rootView, R.id.listviewNews);
        listviewNews.setLayoutManager(new LinearLayoutManager(getContext()));
        listviewNews.setItemAnimator(new DefaultItemAnimator());
        DividerLine dividerLine = new DividerLine();
        dividerLine.setColor(Color.BLACK);
        listviewNews.addItemDecoration(dividerLine);

        textJson = ButterKnife.findById(rootView, R.id.textJson);
        lLayoutDownload = ButterKnife.findById(rootView, R.id.lLayoutDownload);
        barDownload = ButterKnife.findById(rootView, R.id.barDownload);
    }

    public void loadNews(List<News> newsList) {
        setEmptyLayoutState(EmptyLayout.HIDE);
        if (textJson.getVisibility() == View.VISIBLE){
            textJson.setVisibility(View.GONE);
        }
        if (lLayoutDownload.getVisibility() == View.VISIBLE){
            lLayoutDownload.setVisibility(View.GONE);
        }
        if (listviewNews.getVisibility() == View.GONE){
            listviewNews.setVisibility(View.VISIBLE);
        }
        if (newsAdapter == null) {
            newsAdapter = new NewsAdapter(getContext(), newsList);
            listviewNews.setAdapter(newsAdapter);
        } else {
            newsAdapter.replaceAll(newsList);
        }
    }

    public void loadJson(String json){
        setEmptyLayoutState(EmptyLayout.HIDE);
        if (textJson.getVisibility() == View.GONE){
            textJson.setVisibility(View.VISIBLE);
        }
        if (lLayoutDownload.getVisibility() == View.VISIBLE){
            lLayoutDownload.setVisibility(View.GONE);
        }
        if (listviewNews.getVisibility() == View.VISIBLE){
            listviewNews.setVisibility(View.GONE);
        }
        textJson.setText(json);
    }

    public void download(){
        setEmptyLayoutState(EmptyLayout.HIDE);
        if (textJson.getVisibility() == View.VISIBLE){
            textJson.setVisibility(View.GONE);
        }
        if (lLayoutDownload.getVisibility() == View.GONE){
            lLayoutDownload.setVisibility(View.VISIBLE);
        }
        if (listviewNews.getVisibility() == View.VISIBLE){
            listviewNews.setVisibility(View.GONE);
        }
    }

    public void downloadProgress(int max, int progress){
        barDownload.setMax(max);
        barDownload.setProgress(progress);
    }

    public void setEmptyLayoutState(int state){
        emptyLayout.setErrorType(state);
    }
}
