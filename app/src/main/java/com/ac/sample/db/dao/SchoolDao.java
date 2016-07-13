package com.ac.sample.db.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.ac.sample.model.School;
import com.ac.sample.db.DemoSQLiteOpenHelper;
import com.android.common.db.dao.ACDBDaoImpl;

/**
 * Created by yangtao on 2016/5/17.
 */
public class SchoolDao extends ACDBDaoImpl<School>{

    public SchoolDao() {
        super(School.class);
    }

    @Override
    public SQLiteOpenHelper getSQLiteOpenHelper() {
        return DemoSQLiteOpenHelper.instance();
    }
}
