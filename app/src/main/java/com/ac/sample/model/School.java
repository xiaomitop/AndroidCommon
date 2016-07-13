package com.ac.sample.model;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.db.helper.BaseColumns;

/**
 * Created by yangtao on 2016/5/14.
 */
@Table(name = "school")
public class School {
    @Id
    @Column(name = BaseColumns._ID)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "school_num")
    private String schoolNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", schoolNum='" + schoolNum + '\'' +
                '}';
    }
}
