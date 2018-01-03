package com.dong.statistics.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <dr_dong>
 * @time 2017/3/15 16:50
 */
public class PreferencesProvider extends BasePreferencesProvider {

    // putString()方法标识
    public static final String METHOD_PUT_STRING = "put_string";
    // getString()方法标识
    public static final String METHOD_GET_STRING = "get_string";
    // putBoolean()方法标识
    public static final String METHOD_PUT_BOOLEAN = "put_boolean";
    // getBoolean()方法标识
    public static final String METHOD_GET_BOOLEAN = "get_boolean";
    // putInt()方法标识
    public static final String METHOD_PUT_INT = "put_int";
    // getInt()方法标识
    public static final String METHOD_GET_INT = "get_int";
    // putFloat()方法标识
    public static final String METHOD_PUT_FLOAT = "put_float";
    // getFloat()方法标识
    public static final String METHOD_GET_FLOAT = "get_float";
    // putLong()方法标识
    public static final String METHOD_PUT_LONG = "put_long";
    // getLong()方法标识
    public static final String METHOD_GET_LONG = "get_long";

    public static final String METHOD_REMOVE = "remove";
    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_DEFAULT_VALUE = "default_value";
    private SharedPreferences mPreferences;

    @Override
    public boolean onCreate() {
        // Provider创建时获取SharedPreferences
        mPreferences = getContext().getSharedPreferences("app_config", Context.MODE_PRIVATE);
        return false;
    }

    @Nullable
    @Override
    public Bundle call(@METHOD_TYPE String method, String arg, Bundle extras) {
        // 用于将数据返回给调用方，例如getString()、getBoolean()
        Bundle replyData = null;
        String key = extras.getString(EXTRA_KEY);
        switch (method) {
            case METHOD_PUT_STRING:
                mPreferences.edit().putString(key, extras.getString(EXTRA_VALUE)).apply();
                break;
            case METHOD_GET_STRING:
                replyData = new Bundle();
                replyData.putString(EXTRA_VALUE,
                        mPreferences.getString(key, extras.getString(EXTRA_DEFAULT_VALUE)));
                break;
            case METHOD_PUT_BOOLEAN:
                mPreferences.edit().putBoolean(key, extras.getBoolean(EXTRA_VALUE)).apply();
                break;
            case METHOD_GET_BOOLEAN:
                replyData = new Bundle();
                replyData.putBoolean(EXTRA_VALUE,
                        mPreferences.getBoolean(key, extras.getBoolean(EXTRA_DEFAULT_VALUE)));
                break;

            case METHOD_PUT_INT:
                mPreferences.edit().putInt(key, extras.getInt(EXTRA_VALUE)).apply();
                break;
            case METHOD_GET_INT:
                replyData = new Bundle();
                replyData.putInt(EXTRA_VALUE,
                        mPreferences.getInt(key, extras.getInt(EXTRA_DEFAULT_VALUE, 0)));
                break;

            case METHOD_PUT_FLOAT:
                mPreferences.edit().putFloat(key, extras.getFloat(EXTRA_VALUE)).apply();
                break;
            case METHOD_GET_FLOAT:
                replyData = new Bundle();
                replyData.putFloat(EXTRA_VALUE,
                        mPreferences.getFloat(key, extras.getFloat(EXTRA_DEFAULT_VALUE)));
                break;

            case METHOD_PUT_LONG:
                mPreferences.edit().putLong(key, extras.getLong(EXTRA_VALUE)).apply();
                break;
            case METHOD_GET_LONG:
                replyData = new Bundle();
                replyData.putLong(EXTRA_VALUE,
                        mPreferences.getLong(key, extras.getLong(EXTRA_DEFAULT_VALUE)));
                break;
            case METHOD_REMOVE:
                mPreferences.edit().remove(key).apply();
                break;

        }
        // 将获取到的值返回给调用方，若为put操作，replyData则为null
        return replyData;
    }

    @StringDef({METHOD_PUT_STRING, METHOD_GET_STRING, METHOD_PUT_BOOLEAN, METHOD_GET_BOOLEAN,
            METHOD_PUT_INT, METHOD_GET_INT, METHOD_PUT_FLOAT, METHOD_GET_FLOAT,
            METHOD_PUT_LONG, METHOD_GET_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface METHOD_TYPE {
    }
}
