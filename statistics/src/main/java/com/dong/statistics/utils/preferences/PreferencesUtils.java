package com.dong.statistics.utils.preferences;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author <dr_dong>
 * @time 2017/3/15 16:48
 */
public class PreferencesUtils {

    public static final String SP_URI = "content://%s.%s";
    private static final String TAG = PreferencesUtils.class.getSimpleName();
    private static Uri sUri;
    private static Context context;

    private PreferencesUtils(@NonNull Context context, @NonNull String name) {
        PreferencesUtils.context = context;
        PreferencesUtils.sUri = Uri.parse(String.format(SP_URI, context.getPackageName(), name));
    }

    public static void getInstance(@NonNull Context context, @NonNull String name) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
        } else {
            new PreferencesUtils(context, name);
        }
    }

    private static Context getContext() {
        if (context == null) {
            Log.e(TAG, "PreferencesUtils cannot be instantiated", new UnsupportedOperationException());
        }
        return context;
    }

    public static void put(String key, String value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putString(PreferencesProvider.EXTRA_VALUE, value);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_STRING, null, data);
    }

    public static String get(String key, String defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putString(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        return getContext() == null ? defValue :
                getContext().getContentResolver()
                        .call(sUri, PreferencesProvider.METHOD_GET_STRING, null, data)
                        .getString(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void put(String key, boolean value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putBoolean(PreferencesProvider.EXTRA_VALUE, value);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_BOOLEAN, null, data);
    }

    public static boolean get(String key, boolean defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putBoolean(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        return getContext() == null ? defValue :
                getContext().getContentResolver()
                        .call(sUri, PreferencesProvider.METHOD_GET_BOOLEAN, null, data)
                        .getBoolean(PreferencesProvider.EXTRA_VALUE, defValue);

    }

    public static void put(String key, int value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putInt(PreferencesProvider.EXTRA_VALUE, value);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_INT, null, data);
    }

    public static int get(String key, int defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putInt(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        return getContext() == null ? defValue :
                getContext().getContentResolver()
                        .call(sUri, PreferencesProvider.METHOD_GET_INT, null, data)
                        .getInt(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void put(String key, float value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putFloat(PreferencesProvider.EXTRA_VALUE, value);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_FLOAT, null, data);
    }

    public static float get(String key, float defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putFloat(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        return getContext() == null ? defValue :
                getContext().getContentResolver()
                        .call(sUri, PreferencesProvider.METHOD_GET_FLOAT, null, data)
                        .getFloat(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void put(String key, long value) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putLong(PreferencesProvider.EXTRA_VALUE, value);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_PUT_LONG, null, data);
    }

    public static long get(String key, long defValue) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        data.putLong(PreferencesProvider.EXTRA_DEFAULT_VALUE, defValue);
        return getContext() == null ? defValue :
                getContext().getContentResolver()
                        .call(sUri, PreferencesProvider.METHOD_GET_LONG, null, data)
                        .getLong(PreferencesProvider.EXTRA_VALUE, defValue);
    }

    public static void delete(String key) {
        Bundle data = new Bundle();
        data.putString(PreferencesProvider.EXTRA_KEY, key);
        getContext().getContentResolver().call(sUri, PreferencesProvider.METHOD_REMOVE, null, data);
    }

}
