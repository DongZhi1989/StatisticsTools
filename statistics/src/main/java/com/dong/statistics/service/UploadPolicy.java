package com.dong.statistics.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <dr_dong>
 *         Time : 2017/12/13 11:57
 *         统计上报策略
 */
public class UploadPolicy {

    public static final String TAG = UploadPolicy.class.getSimpleName();
    /**
     * 实时发送
     */
    public static final int UPLOAD_POLICY_REAL_TIME = 0;
    /**
     * 定时上报
     */
    public static final int UPLOAD_POLICY_INTERVAL = 1;
    /**
     * 定时上报，网络控制
     */
    public static final int UPLOAD_POLICY_INTERVAL_NET = 2;
    /**
     * 批量上报
     */
    public static final int UPLOAD_POLICY_BATCH = 3;
    /**
     * 批量上报，网络控制
     */
    public static final int UPLOAD_POLICY_BATCH_NET = 4;
    /**
     * 每次启动，上报上次产生的数据
     */
    public static final int UPLOAD_POLICY_WHILE_INITIALIZE = 5;

    private static UploadPolicy instance;
    private static int uploadPolicyType = UPLOAD_POLICY_REAL_TIME;
    private static int intervalTime = 3;
    private static int batchSize = 100;
    private static int netTimes = 5;
    private static boolean uploadNetControl = false;
    /**
     * 是否弱网环境标志，暂时用是否连接WiFi网络来判断
     */
    private static boolean nowWeakNetFlag = false;

    private UploadPolicy(@NonNull Context context, @UPLOAD_POLICY_TYPE int uploadPolicyType,
                         int intervalTime, int batchSize, int netTimes) {
        setUploadNetControl(false);
        setNetTimes(netTimes);
        setUploadPolicyType(uploadPolicyType);
        switch (uploadPolicyType) {
            case UPLOAD_POLICY_REAL_TIME:
                setIntervalTime(0);
                setBatchSize(1);
                break;
            case UPLOAD_POLICY_INTERVAL:
                setIntervalTime(intervalTime);
                setBatchSize(batchSize == 0 ? 1000 : batchSize);
                break;
            case UPLOAD_POLICY_INTERVAL_NET:
                setUploadNetControl(true);
                setIntervalTime(intervalTime);
                setBatchSize(batchSize == 0 ? 1000 : batchSize);
                break;
            case UPLOAD_POLICY_BATCH:
                setIntervalTime(0);
                setBatchSize(batchSize == 0 ? 1000 : batchSize);
                break;
            case UPLOAD_POLICY_BATCH_NET:
                setUploadNetControl(true);
                setIntervalTime(0);
                setBatchSize(batchSize == 0 ? 1000 : batchSize);
                break;
            case UPLOAD_POLICY_WHILE_INITIALIZE:
                setIntervalTime(0);
                setBatchSize(batchSize == 0 ? 10000 : batchSize);
                break;
            default:
                break;
        }
        Intent intent = new Intent(context, UploadService.class);
        context.startService(intent);
        initReceiver(context, uploadPolicyType);
        if (isUploadNetControl()) {
            NetWorkStateReceiver netWorkStateReceiver = new NetWorkStateReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(netWorkStateReceiver, filter);
        }
    }

    @Nullable
    public static UploadPolicy getInstance() {
        if (instance == null) {
            Log.e(TAG, "UploadPolicy is null", new NullPointerException());
            return null;
        }
        return instance;
    }

    @UPLOAD_POLICY_TYPE
    public static int getUploadPolicyType() {
        return uploadPolicyType;
    }

    public static void setUploadPolicyType(@UPLOAD_POLICY_TYPE int uploadPolicyType) {
        UploadPolicy.uploadPolicyType = uploadPolicyType;
    }

    public static boolean isUploadNetControl() {
        return uploadNetControl;
    }

    public static void setUploadNetControl(boolean uploadNetControl) {
        UploadPolicy.uploadNetControl = uploadNetControl;
    }

    public static int getNetTimes() {
        return netTimes;
    }

    public static void setNetTimes(int netTimes) {
        UploadPolicy.netTimes = netTimes;
    }

    public static void setNowWeakNetFlag(boolean nowWeakNetFlag) {
        UploadPolicy.nowWeakNetFlag = nowWeakNetFlag;
    }

    /**
     * 初始化 UploadPolicy
     *
     * @param context          上下文
     * @param uploadPolicyType 上报策略机制
     * @param intervalTime     时间间隔：单位分钟，为0时实时上报
     * @param batchSize        批量控制
     * @param netTimes         弱网策略控制倍数
     */
    public static void initialize(@NonNull Context context,
                                  @UPLOAD_POLICY_TYPE int uploadPolicyType,
                                  int intervalTime,
                                  int batchSize,
                                  int netTimes) {
        if (instance == null) {
            instance = new UploadPolicy(context, uploadPolicyType, intervalTime, batchSize, netTimes);
        }
    }

    /**
     * 定时上报的另一种实现方式，采用 BroadcastReceiver 接收 Intent.ACTION_TIME_TICK
     *
     * @param context
     * @param uploadPolicyType
     */
    private void initReceiver(@NonNull Context context, @UPLOAD_POLICY_TYPE int uploadPolicyType) {
        if (uploadPolicyType == UPLOAD_POLICY_INTERVAL || uploadPolicyType == UPLOAD_POLICY_INTERVAL_NET) {
            BroadcastReceiver receiver = new UploadReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            context.registerReceiver(receiver, intentFilter);
        }
    }

    public int getIntervalTime() {
        if (uploadNetControl && nowWeakNetFlag) {
            return intervalTime * netTimes;
        } else {
            return intervalTime;
        }
    }

    public static void setIntervalTime(int intervalTime) {
        UploadPolicy.intervalTime = intervalTime;
    }

    public int getBatchSize() {
        if (uploadNetControl && nowWeakNetFlag) {
            return batchSize / netTimes;
        } else {
            return batchSize;
        }
    }

    public static void setBatchSize(int batchSize) {
        UploadPolicy.batchSize = batchSize;
    }

    @IntDef({UPLOAD_POLICY_REAL_TIME, UPLOAD_POLICY_BATCH, UPLOAD_POLICY_INTERVAL,
            UPLOAD_POLICY_BATCH_NET, UPLOAD_POLICY_INTERVAL_NET, UPLOAD_POLICY_WHILE_INITIALIZE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UPLOAD_POLICY_TYPE {
    }

}
