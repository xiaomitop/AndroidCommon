package com.common.recyclerview.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.common.recyclerview.R;
import com.common.recyclerview.adapter.BaseAdapter;
import com.common.recyclerview.demo.adapter.InfoAdapter;
import com.common.recyclerview.demo.bean.InfoBean;
import com.common.recyclerview.refresh.PullBaseView;
import com.common.recyclerview.refresh.PullRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Demo2Activity extends AppCompatActivity implements BaseAdapter.OnItemClickListener, BaseAdapter.OnItemLongClickListener,
        BaseAdapter.OnViewClickListener, PullBaseView.OnRefreshListener {

    PullRecyclerView recyclerView;
    List<Object> mDatas;
    InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        initData();
        initRecyclerView();
    }

    protected void initData() {
        mDatas = new ArrayList<>();
        List<Object> imageList = new ArrayList<>();
        imageList.add("http://avatar.csdn.net/3/B/9/1_baiyuliang2013.jpg");
        imageList.add("https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=a22d53b052fbb2fb2b2b5f127f482043/ac345982b2b7d0a2f7375f70ccef76094a369a65.jpg");
        imageList.add("https://ss3.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=57c485df7cec54e75eec1d1e893a9bfd/241f95cad1c8a786bfec42ef6009c93d71cf5008.jpg");
        imageList.add("https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=f3f6ab70cc134954611eef64664f92dd/dcc451da81cb39db1bd474a7d7160924ab18302e.jpg");
        imageList.add("https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=71cd4229be014a909e3e41bd99763971/472309f7905298221dd4c458d0ca7bcb0b46d442.jpg");
        imageList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1564533037,3918553373&fm=116&gp=0.jpg");
        for (int i = 1; i < 30; i++) {
            InfoBean info = new InfoBean();
            info.setText("TEXTTEXTTEXTTEXTTEXTTEXTTEXTTEXT" + i);
            info.setImgList(imageList);
            mDatas.add(info);
        }
    }

    private void initRecyclerView() {
        recyclerView = (PullRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoAdapter = new InfoAdapter(this, mDatas, this);
        infoAdapter.setOnItemClickListener(this);
        infoAdapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(infoAdapter);
    }

    /**
     * 子View点击事件
     *
     * @param position item position
     * @param viewtype 点击的view的类型，调用时根据不同的view传入不同的值加以区分
     */
    @Override
    public void onViewClick(int position, int viewtype) {
        switch (viewtype) {
            case 1://赞
                Toast.makeText(Demo2Activity.this, "赞-position>>" + position, Toast.LENGTH_SHORT).show();
                break;
            case 2://评论
                Toast.makeText(Demo2Activity.this, "评论-position>>" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * item点击事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(Demo2Activity.this, "点击-position>>" + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * item长按事件
     *
     * @param position
     */
    @Override
    public void onItemLongClick(int position) {
        Toast.makeText(Demo2Activity.this, "长按-position>>" + position, Toast.LENGTH_SHORT).show();
    }

    /**
     * 上拉加载
     *
     * @param view
     */
    @Override
    public void onFooterRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InfoBean info = new InfoBean();
                info.setText("更多更多更多更多更多更多更多更多更多更多更多更多更多更多更多更多更多");
                mDatas.add(info);
                infoAdapter.notifyDataSetChanged();
                recyclerView.onFooterRefreshComplete();
            }
        }, 1500);
    }

    /**
     * 下拉刷新
     *
     * @param view
     */
    @Override
    public void onHeaderRefresh(PullBaseView view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InfoBean info = new InfoBean();
                info.setText("新增新增新增新增新增新增新增新增新增新增新增新增新增新增新增新增新增新增");
                mDatas.add(0, info);
                infoAdapter.notifyDataSetChanged();
                recyclerView.onHeaderRefreshComplete();
            }
        }, 1500);
    }


}
