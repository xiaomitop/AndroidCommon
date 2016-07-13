package com.android.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import com.android.common.log.Logger;

/**
 * Created by yangtao on 2016/6/4.
 */
public class ACReceiverUtils {
    private Context mContext;
    private BroadcastReceiver broadcastReceiver;

    public ACReceiverUtils(Context mContext, BroadcastReceiver broadcastReceiver) {
        this.mContext = mContext;
        this.broadcastReceiver = broadcastReceiver;
    }

    /**
     * 构建器
     */
    public static class Builder {
        private Context mContext;
        private IntentFilter intentFilter;

        public Builder(Context mContext) {
            this.mContext = mContext;
            intentFilter = new IntentFilter();
        }

        public Builder addAction(String action) {
            intentFilter.addAction(action);
            return this;
        }

        /**
         * 注册广播
         */
        public ACReceiverUtils registerReceiver(BroadcastReceiver broadcastReceiver) {
            try {
                mContext.registerReceiver(broadcastReceiver, intentFilter);
            } catch (Exception e) {
                Logger.e("ACReceiverUtils", "registerReceiver error: " + e.toString());
                return null;
            }
            return new ACReceiverUtils(mContext, broadcastReceiver);
        }
    }

    /**
     * 注销广播
     */
    public void unregisterReceiver() {
        try {
            //注销广播
            mContext.unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            Logger.e("ACReceiverUtils", "unregisterReceiver error: " + e.toString());
        }
    }
}
