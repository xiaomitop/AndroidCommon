package com.yt.commdemo.db.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.android.common.db.dao.ACDBDaoImpl;
import com.yt.commdemo.bean.Classes;
import com.yt.commdemo.db.DemoSQLiteOpenHelper;

/**
 * Created by yangtao on 2016/5/17.
 */
public class ClassesDao extends ACDBDaoImpl<Classes>{

    public ClassesDao() {
        super(Classes.class);
    }

    @Override
    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return DemoSQLiteOpenHelper.instance();
    }
}
