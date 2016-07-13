package com.ac.sample;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ac.sample.db.dao.SchoolDao;
import com.ac.sample.db.dao.UserDao;
import com.ac.sample.model.ACClass;
import com.ac.sample.model.School;
import com.ac.sample.model.User;
import com.ac.sample.ormlite.bean.OrmliteUser;
import com.ac.sample.ormlite.db.DatabaseHelper;
import com.android.common.Activity.ACBaseActivity;
import com.android.common.http.OkHttpUtils;
import com.android.common.http.callback.HttpCallback;
import com.android.common.http.callback.ProgressListener;
import com.android.common.http.request.HttpParams;
import com.android.common.log.Logger;
import com.android.common.ui.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends ACBaseActivity {

    @Bind(R.id.download_bar)
    ProgressBar download_bar;
    private Call downloadCall;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.get)
    void get() {
        List<School> schoolList = new ArrayList<>();
        //ACTableHelper.createTable(null, OrmliteUser.class);
        UserDao userDao = new UserDao();
        SchoolDao schoolDao = new SchoolDao();
        User user = new User();
        user.setAge(10);
        user.setClassNum("01");
        user.setEmail("435295045@qq.com");
        user.setName("tyang");
        //user.setId(2);

        ACClass acClass = new ACClass();
        acClass.setClassName("一班");
        acClass.setClassNum("01");
        acClass.setId(2);
        //user.setAcClass(acClass);

        /*School school = new School();
        school.setName("xiaoxu");
        school.setSchoolNum("03");*/
        //school.setId(5);
        //user.setSchool(school);
        //userDao.insert(user);

        //user.setId(2);


        //伪代码
        long startTime = System.currentTimeMillis();   //获取开始时间
        //for (int i = 0; i<1000;i++){
        long id = userDao.insertReturnId(user);
        //userDao.update(user, "name = 'tyang'");
        Logger.e("ssss", "----->  " + id + "   " + userDao.queryOne(id).toString());
        // }
        List<User> userList = userDao.queryList(); //测试的代码
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

        long startTime2 = System.currentTimeMillis();   //获取开始时间
        //queryUserTest();
        /*for (int i = 0; i<1000; i++){
            School school = new School();
            school.setName("xiaoxu"+i);
            school.setSchoolNum(String.valueOf(i));
            schoolList.add(school);
        }
        schoolDao.insertList(schoolList);*/
        //schoolList=schoolDao.queryList();
        long endTime2 = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间：22 " + (endTime2 - startTime2) + "ms");

        for (User s : userList) {
            Logger.e("get", "---->   " + s.toString());
        }


        /*OkHttpUtils.get("https://git.oschina.net/xiaomitop/UpdateApp/raw/master/update/json_update", null,
                new HttpCallback<UpdateAppJson>(UpdateAppJson.class) {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        Logger.e("dddddddd", "=====完成" + t);
                    }

                    @Override
                    public void onSuccess(UpdateAppJson updateAppJson) {
                        super.onSuccess(updateAppJson);
                        Logger.e("onSuccess", "onSuccess---> " + updateAppJson.toString());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Logger.e("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        Logger.e("onFailure", "onFailure------>  " + strMsg + "   " + errorNo);
                    }
                });*/
    }

    /**
     * 增
     */
    public void addUserTest() {
        DatabaseHelper helper = DatabaseHelper.getHelper(MainActivity.this);
        try {
            OrmliteUser user = new OrmliteUser();
            user.setAge(10);
            user.setClassNum("01");
            user.setEmail("435295045@qq.com");
            user.setName("tyang");
            helper.getUserDao().create(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryUserTest() {
        DatabaseHelper helper = DatabaseHelper.getHelper(MainActivity.this);
        List<OrmliteUser> list = null;
        try {
            list = helper.getUserDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.e("get", "---->  222  " + list.size());
        /*if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).toString());
            }
        }*/
    }

    @OnClick(R.id.post)
    public void post() {
        HttpParams params = new HttpParams();
        /*params.putHeaders("cookie", "loginName=test;" +
                "loginPwd=123456;");*/
        params.put("username", "xiaomitop");
        params.put("userpassword", "123456");
        OkHttpUtils.post("http://192.168.1.100:8080/CookieDemo/user/cookie.do"/*"http://app.jhws.top:8081/api/appUser/getUserInfo"*/, params,
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Logger.e("JJJJJJJ", transferredBytes + "=====" + totalSize);
                    }
                }, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.e("onSuccess", "onSuccess----> " + t);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Logger.e("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        Logger.e("error", "error---->  " + strMsg + "   " + errorNo);
                    }
                });
    }

    @OnClick(R.id.post1)
    public void post1() {
        HttpParams params = new HttpParams();
        /*params.putHeaders("cookie", "loginName=test;" +
                "loginPwd=123456;");*/
        params.put("username", "13168035105");
        params.put("userpassword", "userpassword");
        OkHttpUtils.post("http://192.168.1.100:8080/CookieDemo/user/test.do", params,
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Logger.e("JJJJJJJ", transferredBytes + "=====" + totalSize);
                    }
                }, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.e("onSuccess", "onSuccess----> " + t);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Logger.e("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        Logger.e("error", "error---->  " + strMsg + "   " + errorNo);
                    }
                });
    }

    /**
     * 下载
     */
    @OnClick(R.id.download)
    void download() {
        /*downloadCall = OkHttpUtils.download(ACFileUtil.SD_CARD_PATH + "ac/" + "a.apk",
                "http://p.gdown.baidu.com/b46c4690e712b33831fa53d1c7056845ccabc55c85785d47ac53aef86d3bec814602fbcbd7fbbfe5c6cfda60b4441f06d729a40ba6a7bcbd190f3c86bda9911ee2752d5e164babfd0e1d7b863a0e6491952b76a496d680fea201d50efe638eb53870d63377f240811dd1a32a4ba44783b7a82c489c316e795aa95b2c19f77ca43d385d6b2fec6f31195edea33b183dcc5daa895cb9f2a664c68841ca8d8386572f536c1bf0c2306a4dd4e14dc8d7931010ec89c8bafaac0d66de3005442c514499ad6707062c7753491e7317b6168b13f863b76b28644f82f307838e1e34a1d3014b35dba80ca60b300d3b5b648e9226973060b106211f6c", new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Logger.e("download", transferredBytes + "======" + totalSize);
                        download_bar.setMax((int) totalSize);
                        download_bar.setProgress((int) transferredBytes);
                    }
                }, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.e("onSuccess", "====success" + t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Logger.e("onFailure", errorNo + "====failure" + strMsg);
                    }
                });*/

        new MaterialDialog(this).setTitle("MaterialDialog")
                .setMessage(
                        "Hi! This is a MaterialDialog. It's very easy to use, you just new and show() it " +
                                "then the beautiful AlertDialog will show automatically. It is artistic, conforms to Google Material Design." +
                                " I hope that you will like it, and enjoy it. ^ ^")
                        //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("OK", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Ok",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL",
                        new MaterialDialog.DialogOnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,
                                        "Cancel", Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                        // You can change the message anytime.
                        // mMaterialDialog.setTitle("提示");
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Toast.makeText(MainActivity.this,
                                        "onDismiss",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
    }

    @OnClick(R.id.download_stop)
    void downloadStop() {
        new MaterialDialog(this).setTitle("MaterialDialog")
                .isOrdinary(true)
                .setMessage(
                        "Hi! This is a MaterialDialog. It's very easy to use, you just new and show() it " +
                                "then the beautiful AlertDialog will show automatically. It is artistic, conforms to Google Material Design." +
                                " I hope that you will like it, and enjoy it. ^ ^")
                        //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("OK", new MaterialDialog.DialogOnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Ok",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL",
                        new MaterialDialog.DialogOnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,
                                        "Cancel", Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                        // You can change the message anytime.
                        // mMaterialDialog.setTitle("提示");
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Toast.makeText(MainActivity.this,
                                        "onDismiss",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();
        if (downloadCall != null) {
            downloadCall.cancel();
            downloadCall = null;
        }
    }
}
