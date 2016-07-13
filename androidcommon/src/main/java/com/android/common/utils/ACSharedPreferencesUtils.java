package com.android.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.common.log.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by yangtao on 2016/6/5.
 */
public class ACSharedPreferencesUtils {
    private static SharedPreferences sp;
    private static final String TAG = "ACSharedPreferencesUtils";
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {
        sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        private static Method findApplyMethod() {
            try {
                Class<SharedPreferences.Editor> clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                Logger.e(TAG, e.toString());
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                Logger.e(TAG, e.toString());
            }
            editor.commit();
        }
    }

    public static long getLongValue(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public static String getStringValue(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.getString(key, null);
        }
        return null;
    }

    public static int getIntValue(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public static int getIntValueByDefault(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public static boolean getBooleanValue(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            sp.getBoolean(key, false);
        }
        return false;
    }

    public static float getFloatValue(Context context, String key) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public static void putStringValue(Context context, String key, String value) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static void putIntValue(Context context, String key, int value) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public void putBooleanValue(Context context, String key, boolean value) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static void putLongValue(Context context, String key, long value) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static void putFloatValue(Context context, String key, Float value) {
        if (!ACStringUtils.isEmpty(key)) {
            sp = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }
}
