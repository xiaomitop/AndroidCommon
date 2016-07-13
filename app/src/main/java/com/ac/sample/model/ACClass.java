package com.ac.sample.model;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.db.helper.BaseColumns;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 15:49
 */
@Table(name = "class")
public class ACClass {
    @Id
    @Column(name = BaseColumns._ID)
    private int id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_num")
    private String classNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    @Override
    public String toString() {
        return "ACClass{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", classNum='" + classNum + '\'' +
                '}';
    }
}
