package com.dong.statistics.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.service.UploadPolicy;
import com.dong.statistics.utils.preferences.PreferencesUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author <dr_dong>
 *         Time : 2017/11/20 10:01
 *         一些统计使用到的工具集合
 */
public class StatisticsUtils {

    public static final String TAG = StatisticsUtils.class.getSimpleName();
    public static Name activityNames;
    private static StatisticsUtils instance;
    private Context context;

    private StatisticsUtils(@NonNull Context context, @NonNull String name, boolean logSwitch,
                            @UploadPolicy.UPLOAD_POLICY_TYPE int uploadPolicyType,
                            int intervalTime, int batchSize, int netTimes) {
        UnCatchHandler.getInstance(context);
        setContext(context);
        StatisticsUtils.activityNames = new Name();
        PreferencesUtils.getInstance(context, name);
        LogUtils.setLogDebug(logSwitch);
        StatisticsReport.readInfo();
        UploadPolicy.initialize(context, uploadPolicyType, intervalTime, batchSize, netTimes);
        //获取设备唯一id
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PreferencesUtils.put(StatisticsConstant.SK_D_I, tm.getImei());
            } else {
                PreferencesUtils.put(StatisticsConstant.SK_D_I, tm.getDeviceId());
            }
        }
        //获取浏览器信息
        WebView webView = new WebView(context);
        PreferencesUtils.put(StatisticsConstant.SK_U_A, webView.getSettings().getUserAgentString());
        //获取系统当前使用的语言
        PreferencesUtils.put(StatisticsConstant.SK_LAN, Locale.getDefault().getLanguage());
        //获取屏幕宽高
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        PreferencesUtils.put(StatisticsConstant.SK_S_W, dm.widthPixels);
        PreferencesUtils.put(StatisticsConstant.SK_S_H, dm.heightPixels);
        try {
            PreferencesUtils.put(StatisticsConstant.SK_VER, context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context          上下文
     * @param name             数据存储表的名称
     * @param logSwitch        log显示开关
     * @param uploadPolicyType 上报策略类型
     * @param intervalTime     上报间隔时间
     * @param batchSize        上报数据上限
     * @param netTimes         弱网调节系数
     */
    public static void initialize(@NonNull Context context, String name, boolean logSwitch,
                                  @UploadPolicy.UPLOAD_POLICY_TYPE int uploadPolicyType,
                                  int intervalTime, int batchSize, int netTimes) {
        if (TextUtils.isEmpty(name)) {
            instance = new StatisticsUtils(context, "statistics", logSwitch, uploadPolicyType, intervalTime, batchSize,
                    netTimes);
        } else {
            instance = new StatisticsUtils(context, name, logSwitch, uploadPolicyType, intervalTime, batchSize,
                    netTimes);
        }
    }

    public static StatisticsUtils getInstance() {
        return instance;
    }

    public static void setInstance(StatisticsUtils instance) {
        StatisticsUtils.instance = instance;
    }

    public static List<View> getAllChildViews(View view) {
        List<View> allChildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewChild = vp.getChildAt(i);
                allChildren.add(viewChild);
                allChildren.addAll(getAllChildViews(viewChild));
            }
        }
        return allChildren;
    }

    public static Map<String, String> bean2Map(Object obj) {
        Map<String, String> reMap = new HashMap<String, String>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                Field f = obj.getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                Object o = f.get(obj);
                if (o == null) {
                    reMap.put(field.getName(), "");
                } else {
                    reMap.put(field.getName(), (String) o);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public Context getContext() {
        if (context == null) {
            Log.e(TAG, "StatisticsUtils cannot be instantiated", new UnsupportedOperationException());
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 获取R文件类名
     *
     * @return R文件类名
     */
    public String getRClassName() {
        return context.getApplicationContext().getPackageName() + ".R";
    }

    /**
     * 用于路径名称
     */
    public static class Name {
        public String fromName;
        public String toName;
    }

}
