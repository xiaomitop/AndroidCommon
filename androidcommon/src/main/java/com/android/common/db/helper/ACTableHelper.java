package com.android.common.db.helper;

import android.database.sqlite.SQLiteDatabase;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.log.Logger;
import com.android.common.utils.ACStringUtils;

import java.lang.reflect.Field;
import java.sql.Blob;

/**
 * Created by yangtao on 2016/5/12.
 */
public class ACTableHelper {
    private static final String TAG = "ACTableHelper";
    /**
     * 创建表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        if (ACStringUtils.isEmpty(tableName)) {
            Logger.e(TAG, "ERROR: 未注解@Table: " + clazz.getName());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        //  外键
        final StringBuilder foreignSb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (");
        final int titleLen = sb.length();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //没有注解@Column 不创建数据库字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            String columnType;
            if (column.type().equals(""))
                columnType = getColumnType(field.getType());
            else {
                columnType = column.type();
            }
            //ID特殊处理,放置最前面
            if (field.isAnnotationPresent(Id.class)) {
                //实体类定义为Integer类型后不能生成Id异常
                if (((field.getType() == Integer.TYPE) || (field.getType() == Integer.class))
                        && field.getAnnotation(Id.class).type() == BaseColumns.TYPE_INCREMENT) {
                    sb.insert(titleLen, column.name() + " " + columnType + " primary key autoincrement, ");
                } else {
                    sb.insert(titleLen, column.name() + " " + columnType + " primary key, ");
                }
            } else if (column.foreign()) {
                //外键
                foreignHandle(field, sb, foreignSb, column.name());
            } else {
                sb.append(column.name()).append(" ").append(columnType);
                if (column.length() != 0) {
                    sb.append("(").append(column.length()).append(")");
                }
                sb.append(", ");
            }
        }

        //加入外键
        sb.append(foreignSb);
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");
        String sql = sb.toString();
        Logger.d(TAG, "create table [" + tableName + "]: " + sql);
        db.execSQL(sql);
    }

    /**
     * 外键处理
     *
     * @param field 外键field
     */
    private static void foreignHandle(Field field, StringBuilder sb, StringBuilder foreignSb, String foreignName) {
        Class<?> clazz = field.getType();
        while (!Object.class.equals(clazz)) {
            String tableName = null;
            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = clazz.getAnnotation(Table.class);
                tableName = table.name();
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(Id.class)) {
                    Column column = f.getAnnotation(Column.class);
                    String idType;
                    if (column.type().equals(""))
                        idType = getColumnType(f.getType());
                    else {
                        idType = column.type();
                    }
                    sb.append(foreignName)
                            .append(" ")
                            .append(idType)
                            .append(", ");
                    foreignSb.append(" FOREIGN KEY(")
                            .append(foreignName)
                            .append(") REFERENCES ")
                            .append(tableName)
                            .append("(")
                            .append(column.name())
                            .append("), ");
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 删除表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "DROP TABLE IF EXISTS " + tableName;
        Logger.d(TAG, "dropTable[" + tableName + "]:" + sql);
        db.execSQL(sql);
    }

    /**
     * 获取列类型.
     *
     * @param fieldType the field type
     * @return 列类型
     */
    private static String getColumnType(Class<?> fieldType) {
        if (String.class == fieldType) {
            return "TEXT";
        }
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            return "INTEGER";
        }
        if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            return "BIGINT";
        }
        if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            return "FLOAT";
        }
        if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
            return "INT";
        }
        if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            return "DOUBLE";
        }
        if (Blob.class == fieldType) {
            return "BLOB";
        }
        return "TEXT";
    }
}
