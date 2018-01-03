package com.dong.statistics.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.dong.statistics.data.InfoQueue;
import com.dong.statistics.data.StatisticsInfo;
import com.dong.statistics.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author <dr_dong>
 *         Time : 2017/12/25 13:43
 *         采用 BroadcastReceiver 接收 Intent.ACTION_TIME_TICK
 */
public class UploadReceiver extends BroadcastReceiver {

    private static final String TAG = UploadReceiver.class.getSimpleName();
    private Handler handler;
    private int intervalTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "===onReceive===");
        if (!Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            return;
        }
        if (handler == null) {
            handler = new Handler();
        }
        while (!InfoQueue.getInstance().getQueueBackups().isEmpty()) {
            LogUtils.d(TAG, "===备份还原");
            InfoQueue.getInstance().add(InfoQueue.getInstance().getBackups());
        }
        intervalTime--;
        if (intervalTime <= 0) {
            intervalTime = UploadPolicy.getInstance().getIntervalTime();
            batchUpload();
        }
    }

    /**
     * 定量上报控制，添加一个随机延时（59s内），
     * Intent.ACTION_TIME_TICK 是系统整分钟发出的，避免大量设备在集中的时间发送数据
     */
    private void batchUpload() {
        Runnable runnableBatch = new Runnable() {
            @Override
            public void run() {
                if (upload()) {
                    handler.post(this);
                }
            }
        };
        handler.postDelayed(runnableBatch, 1000L * new Random().nextInt(59));
    }

    /**
     * @return true 数据没有上报完，继续；false 上报完毕
     */
    private boolean upload() {
        int batchSize = UploadPolicy.getInstance().getBatchSize();
        List<StatisticsInfo> infoList = new ArrayList<>();
        if (UploadPolicy.getUploadPolicyType() == UploadPolicy.UPLOAD_POLICY_WHILE_INITIALIZE) {
            if (InfoQueue.getInstance().getQueueBackups().isEmpty()) {
                return false;
            }
            while (!InfoQueue.getInstance().getQueueBackups().isEmpty() && batchSize > 0) {
                infoList.add(InfoQueue.getInstance().getBackups());
                batchSize--;
            }
        } else {
            if (batchSize > InfoQueue.getInstance().getQueue().size()) {
                return false;
            }
            while (!InfoQueue.getInstance().getQueue().isEmpty() && batchSize > 0) {
                infoList.add(InfoQueue.getInstance().get(true));
                batchSize--;
            }
        }
        //网络上报
        LogUtils.d(TAG, "网络上报条数===" + infoList.size());
        for (int i = 0; i < infoList.size(); i++) {
            LogUtils.i(TAG, "网络上报===" + i + "===" + infoList.get(i).toString());
        }
        return true;
    }

}
