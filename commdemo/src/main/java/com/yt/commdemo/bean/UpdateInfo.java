package com.yt.commdemo.bean;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class UpdateInfo {
    private String appName;
    private int versionCode;
    private String versionName;
    private String appUrl;
    private String date;
    private String updateTips;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdateTips() {
        return updateTips;
    }

    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" + '\n' +
                "   appName='" + appName + '\'' + '\n' +
                "   versionCode=" + versionCode + '\n' +
                "   versionName='" + versionName + '\'' + '\n' +
                "   appUrl='" + appUrl + '\'' + '\n' +
                "   date='" + date + '\'' + '\n' +
                "   updateTips='" + updateTips + '\'' + '\n' +
                '}';
    }
}
