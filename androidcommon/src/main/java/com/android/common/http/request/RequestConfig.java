package com.android.common.http.request;

public class RequestConfig {

    public int mTimeout = 3000; // 请求超时时间,默认是3s readTimeout|writeTimeout=3000+5000

    public int mCacheTime = 300; //缓存时间(单位：秒、默认：5分钟.服务器端声明了no-cache无效)

    public boolean mUseServerControl; //服务器控制缓存时间,为true时mCacheTime无效

    public int mMethod; // 请求方式

    public Boolean mShouldCache = true; // 是否缓存本次请求,只对get有效okhttp post默认不缓存

    public String mUrl; //请求接口地址

    public String mEncoding = "UTF-8"; //编码
}
