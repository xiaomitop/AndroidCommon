package com.android.common.Activity;

import android.os.Bundle;
import android.view.View;

/**
 * 功能：FrameActivity接口协议，实现此接口可使用ActivityManager堆栈
 * 作者：yangtao
 * 创建时间：2016/4/8 15:45
 */
public interface I_Activity {
    /** 初始化所有成员 */
    void initAllMembersView(Bundle savedInstanceState);

    /** 初始化数据 */
    void initData();

    /**
     * view创建完成
     */
    void viewCreated(View rootView);

    /** 注册类（如回调） */
    void registerMethod();

    /** 取消注册类（如回调） */
    void unRegisterMethod();
}
