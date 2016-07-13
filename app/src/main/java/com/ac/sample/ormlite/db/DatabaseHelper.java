package com.ac.sample.ormlite.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ac.sample.ormlite.bean.OrmliteUser;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // 数据库名字
    private static final String TABLE_NAME = "ormlite-test.db";
    // 表的Dao，每一张表对应一个Dao
    private Dao<OrmliteUser, Integer> userDao;

    // 构造函数，私有的外部不能直接访问
    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            // 通过TableUtils这个类新建User类对应的表
            TableUtils.createTable(connectionSource, OrmliteUser.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // 删除表
            TableUtils.dropTable(connectionSource, OrmliteUser.class, true);
            // 再建表
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DatabaseHelper实例
    private static DatabaseHelper instance;

    /**
     * 单例模式获取实例
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

    /**
     * 获取UserDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<OrmliteUser, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(OrmliteUser.class);
        }
        return userDao;
    }

    /**
     * 释放
     */
    @Override
    public void close() {
        super.close();
        userDao = null;
    }
}

