package com.yt.commdemo.bean;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.db.helper.BaseColumns;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
@Table(name = "classes")
public class Classes {
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
