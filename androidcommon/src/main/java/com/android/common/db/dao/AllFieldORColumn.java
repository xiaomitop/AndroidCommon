package com.android.common.db.dao;

import java.lang.reflect.Field;

/**
 * Created by yangtao on 2016/5/15.
 */
public class AllFieldORColumn {
    private Field field;
    private Field foreignField;
    private Class<?> fieldType;
    private Class<?> foreignFieldType;
    /**
     * 列名.
     *
     * @return the string
     */
    private String name;

    /**
     * 列类型.
     *
     * @return the string
     */
    private String type;

    /**
     * 长度.
     *
     * @return the int
     */
    private int length;

    /**
     * 外键.
     *
     * @return the boolean
     */
    private boolean foreign;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getForeignField() {
        return foreignField;
    }

    public void setForeignField(Field foreignField) {
        this.foreignField = foreignField;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }
    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public Class<?> getForeignFieldType() {
        return foreignFieldType;
    }

    public void setForeignFieldType(Class<?> foreignFieldType) {
        this.foreignFieldType = foreignFieldType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }
}
