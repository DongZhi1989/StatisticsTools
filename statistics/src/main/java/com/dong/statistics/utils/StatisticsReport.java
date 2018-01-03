package com.dong.statistics.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.dong.statistics.data.InfoQueue;
import com.dong.statistics.data.StatisticsConstant;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.preferences.PreferencesUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <dr_dong>
 *         Time : 2017/11/20 10:20
 *         上报统计信息工具类
 */
public class StatisticsReport {

    private StatisticsReport() {
    }

    /**
     * 上传统计信息
     *
     * @param info
     */
    public static void reportCollectInfo(final StatisticsInfo info) {
        //调试阶段信息不上报
//        if (BuildConfig.DEBUG) {
//            return;
//        }
        //获取设备唯一id
        if (TextUtils.isEmpty(PreferencesUtils.get(StatisticsConstant.SK_D_I, null))) {
            TelephonyManager tm = (TelephonyManager) StatisticsUtils.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(StatisticsUtils.getInstance().getContext(),
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                info.setD_i(PreferencesUtils.get(StatisticsConstant.SK_D_I, StatisticsConstant.SV_UNKNOWN));
            } else {
                PreferencesUtils.put(StatisticsConstant.SK_D_I, tm.getDeviceId());
                info.setD_i(PreferencesUtils.get(StatisticsConstant.SK_D_I, StatisticsConstant.SV_UNKNOWN));
            }
        } else {
            info.setD_i(PreferencesUtils.get(StatisticsConstant.SK_D_I, StatisticsConstant.SV_UNKNOWN));
        }
        info.setU_a(PreferencesUtils.get(StatisticsConstant.SK_U_A, StatisticsConstant.SV_UNKNOWN));
        info.setLan(PreferencesUtils.get(StatisticsConstant.SK_LAN, StatisticsConstant.SV_UNKNOWN));
        info.setS_w(PreferencesUtils.get(StatisticsConstant.SK_S_W, 0));
        info.setS_h(PreferencesUtils.get(StatisticsConstant.SK_S_H, 0));
        PreferencesUtils.put(StatisticsConstant.SK_S_X, 0);
        PreferencesUtils.put(StatisticsConstant.SK_S_Y, 0);
        info.setRef(StatisticsUtils.activityNames.fromName);
        info.setUrl(StatisticsUtils.activityNames.toName);
        info.setC_t(System.currentTimeMillis());
        info.setVer(PreferencesUtils.get(StatisticsConstant.SK_VER, StatisticsConstant.SV_UNKNOWN));
        info.setIp(SysUtils.getIpAddressString());
        info.setOs(SysUtils.getOSName());
        info.setO_v(SysUtils.getOSVersion());
        info.setEng(StatisticsConstant.ENG_CHROME);
        info.setMan(TextUtils.isEmpty(SysUtils.getSysManufacturer()) ?
                StatisticsConstant.SV_UNKNOWN : SysUtils.getSysManufacturer());
        info.setMod(TextUtils.isEmpty(SysUtils.getSysModel()) ?
                StatisticsConstant.SV_UNKNOWN : SysUtils.getSysModel());
        info.setLat(SysUtils.getLat(StatisticsUtils.getInstance().getContext()));
        info.setLon(SysUtils.getLon(StatisticsUtils.getInstance().getContext()));
        info.setN_t(SysUtils.getNetType(StatisticsUtils.getInstance().getContext()));
        info.setC_p(StatisticsConstant.C_P_ANDROID);

        try {
            info.setC_d(Settings.System.getInt(StatisticsUtils.getInstance().getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS));
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.i("===", "===" + info.toString());
        InfoQueue.getInstance().add(info);
    }

    /**
     * 自定义字段上报
     *
     * @param context
     * @param customStr
     */
    public static void sendCustom(Object context, String customStr) {
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setE_p(context.getClass().getSimpleName());
        statisticsInfo.setE_d(customStr);
        reportCollectInfo(statisticsInfo);
    }

    /**
     * 获取场景的 code
     *
     * @param url 场景链接
     * @return code
     */
    public static String getSceneCode(String url) {
        url = url.replace("http://", "");
        url = url.replace("https://", "");
        String[] result = url.split("/");
        return result[result.length - 1];
    }

    /**
     * 应用退出保存 InfoQueue 队列，当back键对出应用时应用并没有
     */
    public static void saveInfo() {
        List<StatisticsInfo> infoList = new ArrayList<>();
        while (!InfoQueue.getInstance().getQueue().isEmpty()) {
            infoList.add(InfoQueue.getInstance().get(true));
        }
        int lastSize = PreferencesUtils.get(StatisticsConstant.S_INFO_SIZE, 0);
        PreferencesUtils.put(StatisticsConstant.S_INFO_SIZE, infoList.size() + lastSize);
        for (int i = lastSize; i < infoList.size() + lastSize; i++) {
            PreferencesUtils.put(StatisticsConstant.S_INFO_NUM + i, new Gson().toJson(infoList.get(i - lastSize)));
        }
        Log.w("===", lastSize + "===saveInfo===" + infoList.size());
    }

    /**
     * 应用启动还原 InfoQueue 队列
     */
    public static void readInfo() {
        int infoSize = PreferencesUtils.get(StatisticsConstant.S_INFO_SIZE, 0);
        Log.w("===", "===readInfo===" + infoSize);
        for (int i = 0; i < infoSize; i++) {
            boolean getSuccess = InfoQueue.getInstance().addBackups(new Gson().fromJson(PreferencesUtils.get(
                    StatisticsConstant.S_INFO_NUM + i, null), StatisticsInfo.class));
            if (getSuccess) {
                PreferencesUtils.delete(StatisticsConstant.S_INFO_NUM + i);
            } else {
                i--;
            }
        }
        PreferencesUtils.put(StatisticsConstant.S_INFO_SIZE, 0);
    }

}
