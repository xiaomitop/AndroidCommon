package com.ac.sample.ormlite.bean;

import com.android.common.db.helper.BaseColumns;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 09:35
 */
@DatabaseTable(tableName = "user")
public class OrmliteUser {

    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = "class_num")
    private String classNum;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "age")
    private int age;

    @DatabaseField(columnName = "email")
    private String email;

    // 包含实体的存储，指定外键
    //@DatabaseField(columnName="name")
    //private ACClass acClass;

    //@DatabaseField(columnName="name")
    //private School school;

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

	/*public School getSchool() {
        return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}*/

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

	/*public ACClass getAcClass() {
		return acClass;
	}

	public void setAcClass(ACClass acClass) {
		this.acClass = acClass;
	}*/

    @Override
    public String toString() {
        return "OrmliteUser{" +
                "id=" + id +
                ", classNum='" + classNum + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
