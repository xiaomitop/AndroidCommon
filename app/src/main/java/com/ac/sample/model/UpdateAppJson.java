package com.ac.sample.model;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;

/**
 * Created by yangtao on 2016/5/2.
 */
@Table(name = "update_app_json")
public class UpdateAppJson {

    @Column(name = "app_name")
    private String appName;


    @Id
    @Column(name = "_id")
    int id;

   // @Column(name = "version_code")
    private int versionCode;
   // @Column(name = "version_name")
    private String versionName;
  //  @Column(name = "app_url")
    private String appUrl;
  //  @Column(name = "date")
    private String date;
    @Column(name = "update_tips")
    private String updateTips;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        return "UpdateAppJson{" +
                "appName='" + appName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", date='" + date + '\'' +
                ", updateTips='" + updateTips + '\'' +
                '}';
    }
}
