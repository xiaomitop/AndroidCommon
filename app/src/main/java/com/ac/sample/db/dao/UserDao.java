package com.ac.sample.db.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.ac.sample.model.User;
import com.ac.sample.db.DemoSQLiteOpenHelper;
import com.android.common.db.dao.ACDBDaoImpl;


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
