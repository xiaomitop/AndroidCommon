package com.ac.sample.demo.rxjava;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/7/4 15:24
 */
public class IdEvent {
    private int id;
    private String name;

    public IdEvent(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public IdEvent(int id) {
        this.id = id;
    }

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
}
