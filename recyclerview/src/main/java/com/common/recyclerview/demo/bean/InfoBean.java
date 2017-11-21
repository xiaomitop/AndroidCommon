package com.common.recyclerview.demo.bean;

import java.util.List;

/**
 * Created by baiyuliang on 2016-5-27.
 */
public class InfoBean extends Object {
    private String text;
    private List<Object> imgList;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Object> getImgList() {
        return imgList;
    }

    public void setImgList(List<Object> imgList) {
        this.imgList = imgList;
    }
}
