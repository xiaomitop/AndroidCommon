package com.yt.cbrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.net.URL;

public class BrowserActivity extends Activity {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mRefresh;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mMore;
    private ImageButton mClearData;
    private ImageButton mOpenFile, iButtonRefresh;
    private Button mGo;
    private EditText mUrl;

    private RelativeLayout mMenu;

    private String mHomeUrl = "http://app.html5.qq.com/navi/index";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;
    private final float disable = 0.5f;
    private final float enable = 1.0f;
    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private URL mIntentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
                mHomeUrl = mIntentUrl.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_browser);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);
        initBtnListenser();
        mHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }

    private void changGoForwardButton(WebView view) {
        if (view.canGoBack())
            mBack.setAlpha(enable);
        else
            mBack.setAlpha(disable);
        if (view.canGoForward())
            mForward.setAlpha(enable);
        else
            mForward.setAlpha(disable);
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setAlpha(enable);
            mHome.setEnabled(true);
        }
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //改变progressbar默认进度条的颜色（深红色）为0xff0066cc
        mPageLoadingProgressBar.setProgressDrawable(new ClipDrawable(new ColorDrawable(0xff0066cc), Gravity.START, ClipDrawable.HORIZONTAL));
    }

    private void init() {
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        initProgressBar();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                moreMenuClose();
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mHandler.sendEmptyMessageDelayed(MSG_OPEN_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
				/* mWebView.showLog("test Log"); */
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            ///////////////////////////////////////////////////////////
            //

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView arg0,
                                             ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
                // TODO Auto-generated method stub
                return super.onShowFileChooser(arg0, arg1, arg2);
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
                BrowserActivity.this.uploadFile = uploadFile;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "test"), 0);
            }


            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                // AlertDialog.Builder builder = new Builder(getContext());
                // builder.setTitle("X5内核");
                // builder.setPositiveButton("确定", new
                // DialogInterface.OnClickListener() {
                //
                // @Override
                // public void onClick(DialogInterface dialog, int which) {
                // // TODO Auto-generated method stub
                // dialog.dismiss();
                // }
                // });
                // builder.show();
                // arg3.confirm();
                // return true;
                return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
            }

            /**
             * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
             */
            @Override
            public void onReceivedTitle(WebView arg0, final String arg1) {
                super.onReceivedTitle(arg0, arg1);
            }


            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                } else {
                    if (mPageLoadingProgressBar.getVisibility() == View.GONE)
                        mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                    mPageLoadingProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(webView, newProgress);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                new AlertDialog.Builder(BrowserActivity.this)
                        .setTitle("确定要下载选择文件？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        download(BrowserActivity.this, arg0);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private void moreMenuClose() {
        if (mMenu != null && mMenu.getVisibility() == View.VISIBLE) {
            mMenu.setVisibility(View.GONE);
            mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_normal));
        }
    }

    private void initBtnListenser() {
        mBack = (ImageButton) findViewById(R.id.btnBack1);
        mForward = (ImageButton) findViewById(R.id.btnForward1);
        mRefresh = (ImageButton) findViewById(R.id.btnRefresh1);
        mExit = (ImageButton) findViewById(R.id.btnExit1);
        mHome = (ImageButton) findViewById(R.id.btnHome1);
        mGo = (Button) findViewById(R.id.btnGo1);
        mUrl = (EditText) findViewById(R.id.editUrl1);
        mMore = (ImageButton) findViewById(R.id.btnMore);
        mMenu = (RelativeLayout) findViewById(R.id.menuMore);
        mClearData = (ImageButton) findViewById(R.id.btnClearData);
        mOpenFile = (ImageButton) findViewById(R.id.btnOpenFile);
        iButtonRefresh = (ImageButton) findViewById(R.id.iButtonRefresh);

        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
            mBack.setAlpha(disable);
            mForward.setAlpha(disable);
            mHome.setAlpha(disable);
        }
        mHome.setEnabled(false);


        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null && mWebView.canGoBack())
                    mWebView.goBack();
            }
        });

        mForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null && mWebView.canGoForward())
                    mWebView.goForward();
            }
        });

        mRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null)
                    mWebView.reload();
            }
        });


        mGo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                String url = mUrl.getText().toString();
                mWebView.loadUrl(url);
            }
        });

        mMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMenu.getVisibility() == View.GONE) {
                    mMenu.setVisibility(View.VISIBLE);
                    mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_pressed));
                } else {
                    mMenu.setVisibility(View.GONE);
                    mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_normal));
                }
            }
        });

        mClearData.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
            }
        });

        mOpenFile.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    BrowserActivity.this.startActivityForResult(Intent.createChooser(intent, "choose file"), 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(BrowserActivity.this, "完成", Toast.LENGTH_LONG).show();
                }
            }
        });

        mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                moreMenuClose();
                if (hasFocus) {
                    mGo.setVisibility(View.VISIBLE);
                    mRefresh.setVisibility(View.GONE);
                    if (null == mWebView.getUrl()) return;
                    if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                        mUrl.setText("");
                        mGo.setText("首页");
                        mGo.setTextColor(0X6F0F0F0F);
                    } else {
                        mUrl.setText(mWebView.getUrl());
                        mGo.setText("进入");
                        mGo.setTextColor(0X6F0000CD);
                    }
                } else {
                    mGo.setVisibility(View.GONE);
                    mRefresh.setVisibility(View.VISIBLE);
                    String title = mWebView.getTitle();
                    if (title != null && title.length() > MAX_LENGTH)
                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    else
                        mUrl.setText(title);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        });

        mUrl.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                String url = null;
                if (mUrl.getText() != null) {
                    url = mUrl.getText().toString();
                }

                if (url == null
                        || mUrl.getText().toString().equalsIgnoreCase("")) {
                    mGo.setText("请输入网址");
                    mGo.setTextColor(0X6F0F0F0F);
                } else {
                    mGo.setText("进入");
                    mGo.setTextColor(0X6F0000CD);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }
        });

        mHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null)
                    mWebView.loadUrl(mHomeUrl);
            }
        });


        mExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // but,We should hide this button, otherwise in the version
                // of Android 3 and above, there is a window leak.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    // call requies API Level 11 ( Android 3.0 + )
                    mWebView.getSettings().setDisplayZoomControls(false);
                }
                mWebView.setVisibility(View.GONE);
                finish();
            }
        });

        iButtonRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.reload();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(mWebView);
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                case 1:
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    public static final int MSG_OPEN_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public static void download(Context context, String uri) {
        String name = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //request.setTitle("正在下载"+name);
        request.setAllowedOverRoaming(false);
        //在通知栏中显示
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE );
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name);
        DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadFerence = downManager.enqueue(request);
    }
}
