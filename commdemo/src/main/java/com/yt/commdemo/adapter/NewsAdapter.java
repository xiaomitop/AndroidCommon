package com.yt.commdemo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.common.adapter.ACCommonRecyclerAdapter;
import com.android.common.adapter.BaseAdapterHelper;
import com.android.common.adapter.ImageLoad;
import com.yt.commdemo.R;
import com.yt.commdemo.bean.News;

import java.util.List;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class NewsAdapter extends ACCommonRecyclerAdapter<News>{
    public NewsAdapter(Context context, List<News> data) {
        super(context, data);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_news_adapter;
    }

    @Override
    public void bindData(BaseAdapterHelper helper, int position, News item) {
        helper.setImageLoad(new ImageLoad())
                .setImageUrl(R.id.image, item.getImageUrl())
                .setText(R.id.textvTitle, item.getTitle())
                .setText(R.id.textDescription, item.getDescription())
                .setText(R.id.itemDate, item.getDate());
    }
}
