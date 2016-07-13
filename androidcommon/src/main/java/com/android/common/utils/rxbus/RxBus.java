package com.android.common.utils.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/7/4 13:53
 */
@SuppressWarnings("unchecked")
public class RxBus {
    private static volatile RxBus instance;
    // 主题
    private final Subject bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }
    // 单例RxBus
    public static RxBus instance() {
        RxBus rxBus = instance;
        if (instance == null) {
            synchronized (RxBus.class) {
                rxBus = instance;
                if (instance == null) {
                    rxBus = new RxBus();
                    instance = rxBus;
                }
            }
        }
        return rxBus;
    }
    // 提供了一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }
    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObserverable (Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
