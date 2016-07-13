package com.android.common.db.helper;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 13:33
 */
public interface BaseColumns {

    /**
     * The unique ID for a row.
     * <P>Type: INTEGER (long)</P>
     */
    String _ID = "_id";

    /** 主键自增. */
    int TYPE_INCREMENT = 1;

    /** 主键自己插入. */
    int TYPE_NOT_INCREMENT = 0;
}
