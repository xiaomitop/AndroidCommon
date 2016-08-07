package com.yt.commdemo.db.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.android.common.db.dao.ACDBDaoImpl;
import com.yt.commdemo.bean.User;
import com.yt.commdemo.db.DemoSQLiteOpenHelper;


/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 14:56
 */
public class UserDao extends ACDBDaoImpl<User> {

    public UserDao(){
        super(User.class);
    }

    @Override
    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return DemoSQLiteOpenHelper.instance();
    }
}
