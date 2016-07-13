package com.ac.sample.demo.rxjava;

import android.os.Bundle;
import android.widget.Button;

import com.ac.sample.R;
import com.android.common.Activity.ACBaseActivity;
import com.android.common.log.Logger;
import com.android.common.utils.rxbus.RxBus;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/7/4 14:17
 */
public class RxAcitivity extends ACBaseActivity{

    private Subscription rxSubscription;
    @Bind(R.id.post)
    Button post;

    @Override
    public int getContentViewId() {
        return R.layout.activity_rx;
    }

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);

        // rxSubscription是一个Subscription的全局变量，这段代码可以在onCreate/onStart等生命周期内
        rxSubscription = RxBus.instance().toObserverable(UserEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserEvent>() {
                               @Override
                               public void call(UserEvent userEvent) {
                                   long id = userEvent.getId();
                                   String name = userEvent.getName();
                                   post.setText("get");
                                   Logger.e("------------>  ", "DDD---> id: " + id + "  name: " + name);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                            }
                        });

        rxSubscription = RxBus.instance().toObserverable(IdEvent.class)
                .subscribe(new Action1<IdEvent>() {
                               @Override
                               public void call(IdEvent userEvent) {
                                   long id = userEvent.getId();
                                   post.setText("get");
                                   //String name = userEvent.getName();
                                   Logger.e("------------>  ", "id---> id: " + id);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                                Logger.e("throwable", "---->   " + throwable.toString());
                            }
                        });
    }

    @OnClick(R.id.post)
    public void post(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RxBus.instance().post(new IdEvent(1, "tyng"));
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }
}
