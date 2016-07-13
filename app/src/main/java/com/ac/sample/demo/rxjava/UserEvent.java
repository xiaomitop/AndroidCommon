package com.ac.sample.demo.rxjava;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/7/4 15:19
 */
public class UserEvent {
    private long id;
    private String name;

    public UserEvent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
