package com.android.common.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * 功能：log统一管理类
 * 作者：yangtao
 * 创建时间：2016/4/8 14:33
 */
public class Logger {
    public static int LOG_LEVEL     = 5;
    public static final int VERBOSE = 5;
    public static final int DEBUG   = 4;
    public static final int INFO    = 3;
    public static final int WARN    = 2;
    public static final int ERROR   = 1;
    public static final String SEPARATOR = ",";

    public static void v(String message) {
        if (LOG_LEVEL >= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.v(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void v(String tag, String message) {
        if (LOG_LEVEL >= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.v(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void d(String message) {
        if (LOG_LEVEL >= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.d(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void d(String tag, String message) {
        if (LOG_LEVEL >= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.d(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void i(String message) {
        if (LOG_LEVEL >= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.i(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void i(String tag, String message) {
        if (LOG_LEVEL >= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.i(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void w(String message) {
        if (LOG_LEVEL >= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            Log.w(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void w(String tag, String message) {
        if (LOG_LEVEL >= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.w(tag, getLogInfo(stackTraceElement) +"\n"+  message);
        }
    }

    public static void e(String tag, String message) {
        if (LOG_LEVEL >= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) +"\n"+ message);
        }
    }

    /**
     * 获取默认的TAG名称.
     * 比如在MainActivity.java中调用了日志输出.
     * 则TAG为MainActivity
     */
    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String fileName = stackTraceElement.getFileName();
        String stringArray[] = fileName.split("\\.");
        return stringArray[0];
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
        logInfoStringBuilder.append("threadID=").append(threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=").append(threadName).append(SEPARATOR);
        logInfoStringBuilder.append("fileName=").append(fileName).append(SEPARATOR);
        logInfoStringBuilder.append("className=").append(className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=").append(methodName).append(SEPARATOR);
        logInfoStringBuilder.append("lineNumber=").append(lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }
}
