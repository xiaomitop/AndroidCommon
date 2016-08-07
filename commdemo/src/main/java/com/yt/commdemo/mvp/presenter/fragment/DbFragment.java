package com.yt.commdemo.mvp.presenter.fragment;

import android.os.Bundle;

import com.android.common.log.Logger;
import com.android.common.mvp.presenter.FragmentPresenterImpl;
import com.yt.commdemo.R;
import com.yt.commdemo.bean.Classes;
import com.yt.commdemo.bean.User;
import com.yt.commdemo.db.dao.UserDao;
import com.yt.commdemo.mvp.view.DbFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class DbFragment extends FragmentPresenterImpl<DbFragmentView> {
    private UserDao userDao;
    private List<User> selectedList;

    @Override
    public void initAllMembersView(Bundle savedInstanceState) {
        super.initAllMembersView(savedInstanceState);
        userDao = new UserDao();
        selectedList = new ArrayList<>();
    }

    @OnClick(R.id.btnAddOne)
    public void addOne() {
        User user = new User();
        user.setAge(10);
        user.setClassNum("01");
        user.setEmail("435295045@qq.com");
        user.setName("tyang");
        //user.setId(2);

        Classes acClass = new Classes();
        acClass.setClassName("一班");
        acClass.setClassNum("01");
        acClass.setId(2);
        user.setClasses(acClass);
        userDao.insertReturnId(user);
        query();
    }

    @OnClick(R.id.btnAdd10000)
    public void add10000() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            User user = new User();
            user.setAge(10);
            user.setClassNum("01");
            user.setEmail("435295045@qq.com");
            user.setName("tyang");
            //user.setId(2);

            Classes acClass = new Classes();
            acClass.setClassName("一班");
            acClass.setClassNum("01");
            acClass.setId(2);
            user.setClasses(acClass);
            users.add(user);
        }
        userDao.insertList(users);
        query();
    }

    @OnClick(R.id.btnDeleteOne)
    public void deleteOne() {
        for (User user : selectedList) {
            userDao.delete(user.getId());
        }
        mView.deleteOne();
        query();
    }

    @OnClick(R.id.btnDeleteAll)
    public void deleteAll() {
        userDao.deleteAll();
        query();
    }

    public void query() {
        Logger.e("------------>  ", "KKKKKKKKKKKKKK--->   1  ");
        sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.newThread())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        Logger.e("------------>  ", "KKKKKKKKKKKKKK--->   3  " + users.size());
                        mView.notifyDataSetChanged(users, selectedList);
                    }
                });
    }

    Observable<List<User>> sampleObservable() {
        return Observable.defer(new Func0<Observable<List<User>>>() {
            @Override
            public Observable<List<User>> call() {
                List<User> users = userDao.queryList();
                Logger.e("------------>  ", "KKKKKKKKKKKKKK--->   2  " + users.size());
                return Observable.just(users);
            }
        });
    }
}
