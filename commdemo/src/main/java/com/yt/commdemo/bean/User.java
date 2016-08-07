package com.yt.commdemo.bean;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.db.helper.BaseColumns;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 09:35
 */
@Table(name = "user")
public class User {

    @Id
    @Column(name = BaseColumns._ID)
    private int id ;

    @Column(name = "class_num")
    private String classNum;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    private String email;

    // 包含实体的存储，指定外键
    @Column(name = "class_id", foreign = true)
    private Classes classes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", classNum='" + classNum + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", classes=" + classes +
                '}';
    }
}
