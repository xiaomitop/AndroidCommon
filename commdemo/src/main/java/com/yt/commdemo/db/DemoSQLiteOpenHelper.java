package com.yt.commdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.common.db.helper.ACTableHelper;
import com.android.common.log.Logger;
import com.yt.commdemo.application.MyApplication;
import com.yt.commdemo.bean.Classes;
import com.yt.commdemo.bean.User;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 09:34
 */
public class DemoSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String dbName = "dborm";
    private static DemoSQLiteOpenHelper instance;

    public static DemoSQLiteOpenHelper instance() {
        if (instance == null) {
            instance = new DemoSQLiteOpenHelper(MyApplication.instance().getContext(), dbName, null, 1);
        }
        return instance;
    }

    public DemoSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ACTableHelper.createTable(sqLiteDatabase, Classes.class);
        ACTableHelper.createTable(sqLiteDatabase, User.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
