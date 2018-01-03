package com.dong.statistics.utils;

import android.util.Log;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/24.
 * 描述：日志输出工具类
 */

public class LogUtils {

    private static final String TAG = LogUtils.class.getSimpleName();

    /**
     * 调试开关,日志控制,默认打开,发布时关闭
     */
    private static boolean isDebug = true;

    public static void setLogDebug(boolean logDebug) {
        isDebug = logDebug;
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.e(tag, msg, tr);
        }
    }

}
