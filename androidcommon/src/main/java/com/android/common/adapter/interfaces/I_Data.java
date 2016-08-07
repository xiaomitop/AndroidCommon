package com.android.common.adapter.interfaces;

import java.util.List;

/**
 * 项目名称: CommonAdapter
 * 包 名 称: com.classic.adapter
 * 类 描 述: 数据操作接口规范
 */
public interface I_Data<T> {

    void add(T elem);

    void addAll(List<T> elem);

    void set(T oldElem, T newElem);

    void set(int index, T elem);

    void remove(T elem);

    void remove(int index);

    void replaceAll(List<T> elem);

    boolean contains(T elem);

    List<T> getData();

    void clear();

}
