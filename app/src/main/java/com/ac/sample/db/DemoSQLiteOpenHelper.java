package com.ac.sample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ac.sample.application.MyApp;
import com.ac.sample.model.*;
import com.android.common.db.helper.ACTableHelper;

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
            instance = new DemoSQLiteOpenHelper(MyApp.instance().getContext(), dbName, null, 1);
        }
        return instance;
    }

    public DemoSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ACTableHelper.createTable(sqLiteDatabase, School.class);
        ACTableHelper.createTable(sqLiteDatabase, ACClass.class);
        ACTableHelper.createTable(sqLiteDatabase, User.class);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
