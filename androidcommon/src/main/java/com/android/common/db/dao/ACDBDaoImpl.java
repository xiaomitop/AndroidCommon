package com.android.common.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.common.db.annotation.Column;
import com.android.common.db.annotation.Id;
import com.android.common.db.annotation.Table;
import com.android.common.db.helper.BaseColumns;
import com.android.common.log.Logger;
import com.android.common.utils.ACStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/13 10:50
 */
@SuppressWarnings("unchecked")
public abstract class ACDBDaoImpl<T> implements I_Dao<T> {
    private static final String TAG = "ACDBDaoImpl";
    public static boolean DEBUG = Logger.LOG_LEVEL >= 4;

    //数据库对象
    private SQLiteDatabase db = null;
    //当前dao类的class
    private Class<T> clazz;
    //表名
    private String tableName;
    //所有Field,Column
    private List<AllFieldORColumn> fieldORColumnList;
    //id字段
    private String idColumn;

    public ACDBDaoImpl(Class<T> clazz) {
        db = getSQLiteOpenHelper().getReadableDatabase();
        fieldORColumnList = new ArrayList<>();
        if (clazz == null) {
            this.clazz = ((Class<T>) ((ParameterizedType) super
                    .getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0]);
        } else {
            this.clazz = clazz;
        }

        if (this.clazz.isAnnotationPresent(Table.class)) {
            Table table = this.clazz.getAnnotation(Table.class);
            this.tableName = table.name();
        }

        Field[] allFields = this.clazz.getDeclaredFields();
        AllFieldORColumn allFieldORColumn;
        // 找到ID，field，Column， Foreign ID装入AllFieldORColumn
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                allFieldORColumn = new AllFieldORColumn();
                Column column = field.getAnnotation(Column.class);
                allFieldORColumn.setName(column.name());
                allFieldORColumn.setForeign(column.foreign());
                allFieldORColumn.setLength(column.length());
                allFieldORColumn.setType(column.type());
                allFieldORColumn.setField(field);
                allFieldORColumn.setFieldType(field.getType());
                if (field.isAnnotationPresent(Id.class)) {
                    this.idColumn = column.name();
                }
                //存入Foreign的ID
                if (allFieldORColumn.isForeign()) {
                    Field[] fields = allFieldORColumn.getFieldType().getDeclaredFields();
                    for (Field f : fields) {
                        if (f.isAnnotationPresent(Id.class)) {
                            f.setAccessible(true);
                            allFieldORColumn.setForeignField(f);
                            allFieldORColumn.setForeignFieldType(f.getType());
                            break;
                        }
                    }
                }
                fieldORColumnList.add(allFieldORColumn);
            }
        }

        if (DEBUG) Logger.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName
                + " idColumn:" + this.idColumn);
    }

    public abstract SQLiteOpenHelper getSQLiteOpenHelper();

    @Override
    public void insert(T entity) {
        String sql;
        try {
            sql = buildInsertSql(entity);
            if (DEBUG) Logger.d(TAG, "[insert]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.e(TAG, "[insert] into DB Exception: " + e.toString());
        }
    }

    @Override
    public long insertReturnId(T entity) {
        int id = -1;
        Cursor cursor = null;
        try {
            String sql = buildInsertSql(entity);
            if (DEBUG) Logger.d(TAG, "[insert]: insert into " + this.tableName + " " + sql);
            db.execSQL(sql);
            cursor = db.rawQuery("select LAST_INSERT_ROWID() from " + this.tableName, null);
            cursor.moveToFirst();
            id = cursor.getInt(0);
        } catch (Exception e) {
            Logger.e(TAG, "[insert] into DB Exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return id;
    }

    @Override
    public void insertIfNotExist(T entity) {
        if (!isExist(entity)) {
            insert(entity);
        }
    }

    @Override
    public void insertList(List<T> entityList) {
        //开启事务
        db.beginTransaction();
        try {
            //批量处理操作
            for (T entity : entityList) {
                db.execSQL(buildInsertSql(entity));
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        } finally {
            //结束事务
            db.endTransaction();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String where = this.idColumn + " = " + "'" + Integer.toString(id) + "'";
            String sql = buildDeleteSql(where);
            if (DEBUG) Logger.d(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.d(TAG, "[delete] DB Exception: " + e.toString());
        }
    }

    @Override
    public void delete(String where) {
        try {
            String sql = buildDeleteSql(where);
            if (DEBUG) Logger.e(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception ex) {
            Logger.e(TAG, ex.toString());
        }
    }

    @Override
    public void deleteAll() {
        try {
            String sql = buildDeleteSql(null);
            if (DEBUG) Logger.d(TAG, "[delete]: " + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.d(TAG, "[delete] DB Exception：" + e.toString());
        }
    }

    @Override
    public void update(T entity) {
        try {
            String sql = buildUpdateSql(entity, null);
            if (DEBUG) Logger.d(TAG, "[update] :" + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.e(TAG, "[update] DB Exception: " + e.toString());
        }
    }

    @Override
    public void update(T entity, String where) {
        try {
            String sql = buildUpdateSql(entity, where);
            if (DEBUG) Logger.d(TAG, "[update] :" + sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.e(TAG, "[update] DB Exception: " + e.toString());
        }
    }

    @Override
    public int update(ContentValues contentValues, String where, String[] whereValue) {
        int rows = 0;
        try {
            rows = db.update(this.tableName, contentValues, where, whereValue);
        } catch (Exception e) {
            Logger.e(TAG, "[update] DB Exception: " + e.toString());
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public void insertSaveOrUpdate(T entity) {
        if (isExist(entity)) {
            update(entity);
        } else {
            insert(entity);
        }
    }

    @Override
    public List<T> queryList() {
        return queryList(null, null, null, null, null, null, null);
    }

    @Override
    public T queryOne(long id) {
        String selection = this.idColumn + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        List<T> list = queryList(null, selection, selectionArgs, null, null, null,
                null);
        if ((list != null) && (list.size() > 0)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<T> queryList(String selection, String[] selectionArgs) {
        return queryList(null, selection, selectionArgs, null, null, null, null);
    }

    @Override
    public int queryCount(String selection, String[] selectionArgs) {
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(this.tableName, null, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception e) {
            Logger.e(TAG, "[queryCount] from DB exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @Override
    public List<T> rawQuery(String sql, String[] selectionArgs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            for (String s : selectionArgs) {
                sql = sql.replaceFirst("\\?", "'" + String.valueOf(s) + "'");
            }
            if (DEBUG) Logger.d(TAG, "[rawQuery]: " + sql);
            cursor = db.rawQuery(sql, selectionArgs);
            executeQuery(list, cursor);
        } catch (Exception e) {
            Logger.e(TAG, "[rawQuery] from DB Exception: " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public List<T> queryList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ")
                .append(this.tableName);
        if (!ACStringUtils.isEmpty(selection)) {
            sb.append(" where (")
                    .append(selection)
                    .append(")");
        }
        if (!ACStringUtils.isEmpty(groupBy)) {
            sb.append(" group by ")
                    .append(groupBy);
        }
        if (!ACStringUtils.isEmpty(having)) {
            sb.append(" having ")
                    .append(having);
        }
        if (!ACStringUtils.isEmpty(orderBy)) {
            sb.append(" order by ")
                    .append(orderBy);
        }
        if (!ACStringUtils.isEmpty(limit)) {
            sb.append(" limit ")
                    .append(limit);
        }
        try {
            String sql = sb.toString();
            if (DEBUG) Logger.d(TAG, "[queryList] : " + sql);
            //cursor = db.query(this.tableName, columns, selection,selectionArgs, groupBy, having, orderBy, limit);
            cursor = db.rawQuery(sql, selectionArgs);
            executeQuery(list, cursor);
        } catch (Exception e) {
            Logger.e(TAG, "[queryList] from DB Exception：" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public boolean isExist(T obj) {
        Cursor cursor = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(*) from ").append(this.tableName).append(" where (");
            List<String> strings = new ArrayList<>();

            Field[] fields = obj.getClass().getDeclaredFields();
            Column column;
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Column.class)) continue;//如果不是注解字段循环下一个
                column = field.getAnnotation(Column.class);
                if (column.foreign()) continue;//这里默认不去比较关联外键
                String selectionArg = field.get(obj).toString();
                if (!ACStringUtils.isEmpty(selectionArg)) {
                    sb.append(column.name()).append("=?").append(" and ");
                    strings.add(selectionArg);
                }
            }
            sb.delete(sb.length() - 5, sb.length() - 1).append(")");
            String sql = sb.toString();
            if (DEBUG) Logger.d(TAG, "[isExist]sql: " + sql);
            cursor = db.rawQuery(sql, strings.toArray(new String[strings.size()]));
            if (null != cursor && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public boolean isExist(String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = db.query(this.tableName, null, selection, selectionArgs, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    @Override
    public void execSql(String sql, Object[] selectionArgs) {
        try {
            db.execSQL(sql, selectionArgs);
        } catch (Exception ex) {
            Logger.e(TAG, ex.toString());
        }
    }

    /**
     * 构造更新sql语句.
     *
     * @param entity 映射实体
     * @return sql语句
     */
    private String buildUpdateSql(final T entity, String where) throws IllegalAccessException {
        StringBuilder updateSql = new StringBuilder("update " + this.tableName + " set ");

        Field field;
        Object fieldValue;
        String value;
        for (AllFieldORColumn fieldORColumn : fieldORColumnList) {
            field = fieldORColumn.getField();
            fieldValue = field.get(entity);
            if (fieldValue == null)
                continue;
            if (!fieldORColumn.isForeign()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if(where == null) {
                        where = this.idColumn + " = " + "'" + String.valueOf(fieldValue) + "'";
                    }
                    continue;
                }
                value = String.valueOf(fieldValue);
            } else {
                //取出关联表主键
                Object foreignValue = fieldORColumn.getForeignField().get(fieldValue);
                value = String.valueOf(foreignValue);
            }
            updateSql.append(fieldORColumn.getName()).append("=").append("'").append(
                    value).append("',");
        }
        return updateSql.deleteCharAt(updateSql.length() - 1)
                .append(" where ")
                .append(where)
                .toString();
    }

    /**
     * 构造插入sql语句
     *
     * @param entity 映射实体
     * @return sql语句
     */
    private String buildInsertSql(final T entity) throws IllegalAccessException {
        StringBuilder insertSql = new StringBuilder("insert into " + this.tableName + " (");
        StringBuilder valueSb = new StringBuilder(" values (");

        String value;
        Field field;
        Object fieldValue;
        for (AllFieldORColumn fieldORColumn : fieldORColumnList) {
            field = fieldORColumn.getField();
            fieldValue = field.get(entity);
            if (fieldValue == null) {
                continue;
            }
            if (!fieldORColumn.isForeign()) {
                if ((field.isAnnotationPresent(Id.class)
                        && field.getAnnotation(Id.class).type() == BaseColumns.TYPE_INCREMENT)) {
                    continue;
                }
                value = String.valueOf(fieldValue);
            } else {
                //取出关联表主键
                Object foreignValue = fieldORColumn.getForeignField().get(fieldValue);
                value = String.valueOf(foreignValue);
            }
            insertSql.append(fieldORColumn.getName()).append(",");
            valueSb.append("'").append(value).append("',");
        }
        insertSql.deleteCharAt(insertSql.length() - 1).append(")");
        valueSb.deleteCharAt(valueSb.length() - 1).append(")");
        return insertSql.append(valueSb).toString();
    }

    /**
     * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
     *
     * @param strWhere 删除条件
     * @return sql语句
     */
    public String buildDeleteSql(String strWhere) {
        StringBuilder strSQL = new StringBuilder("delete from " + this.tableName);
        if (!ACStringUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }
        return strSQL.toString();
    }

    /**
     * 查询数据库表并自动装箱到clazz对象中
     *
     * @throws Exception
     */
    public void executeQuery(List<T> list, Cursor cursor) throws Exception {
        Object entity;
        Field field;
        while (cursor.moveToNext()) {
            entity = clazz.newInstance();
            for (AllFieldORColumn fieldORColumn : fieldORColumnList) {
                field = fieldORColumn.getField();
                int c = cursor.getColumnIndex(fieldORColumn.getName());
                if (c < 0) continue; // 如果不存在则循环下个属性值
                //如果是关联关系就先不加载关联数据库表，只返回外键
                if (fieldORColumn.isForeign()) {
                    Object foreignEntity = fieldORColumn.getFieldType().newInstance();
                    if (!cursor.isNull(c)) {
                        executeField(fieldORColumn.getForeignField(), foreignEntity, cursor, fieldORColumn.getForeignFieldType(), c);
                        field.set(entity, foreignEntity);
                    }
                } else {
                    executeField(field, entity, cursor, fieldORColumn.getFieldType(), c);
                }
            }
            list.add((T) entity);
        }
    }

    public void executeField(Field field, Object entity, Cursor cursor, Class<?> fieldType, int c) throws Exception {
        if ((Integer.TYPE == fieldType)
                || (Integer.class == fieldType)) {
            field.set(entity, cursor.getInt(c));
        } else if (String.class == fieldType) {
            field.set(entity, cursor.getString(c));
        } else if ((Long.TYPE == fieldType)
                || (Long.class == fieldType)) {
            field.set(entity, cursor.getLong(c));
        } else if ((Float.TYPE == fieldType)
                || (Float.class == fieldType)) {
            field.set(entity, cursor.getFloat(c));
        } else if ((Short.TYPE == fieldType)
                || (Short.class == fieldType)) {
            field.set(entity, cursor.getShort(c));
        } else if ((Double.TYPE == fieldType)
                || (Double.class == fieldType)) {
            field.set(entity, cursor.getDouble(c));
        } else if (Date.class == fieldType) {
            Date date = new Date();
            date.setTime(cursor.getLong(c));
            field.set(entity, date);
        } else if (Blob.class == fieldType) {
            field.set(entity, cursor.getBlob(c));
        } else if (Character.TYPE == fieldType) {
            String fieldValue = cursor.getString(c);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                field.set(entity, fieldValue.charAt(0));
            }
        } else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
            String temp = cursor.getString(c);
            if ("true".equals(temp) || "1".equals(temp)) {
                field.set(entity, true);
            } else {
                field.set(entity, false);
            }
        }
    }
}
